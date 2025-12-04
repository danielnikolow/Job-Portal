package app.web;

import app.model.Application;
import app.model.User;
import app.repository.ApplicationRepository;
import app.repository.UserRepository;
import app.security.UserData;
import app.service.ApplicationService;
import app.service.JobService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class RecruiterApplicationController {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationService applicationService;
    private final JobService jobService;

    public RecruiterApplicationController(UserRepository userRepository, ApplicationRepository applicationRepository, ApplicationService applicationService, JobService jobService) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.applicationService = applicationService;
        this.jobService = jobService;
    }

    @GetMapping("/recruiter-applications")
    public ModelAndView getRecruiterApplicationPage(@AuthenticationPrincipal UserData userData) {

        ModelAndView modelAndView = new ModelAndView();
        Optional<User> user = userRepository.findById(userData.getUserId());
        User curUser = user.get();

        List<Application> allApplications = applicationRepository.findAll();
        List<Application> activeApplications = allApplications.stream()
                .filter(app -> app.getJob() != null && app.getJob().isActive())
                .toList();

        modelAndView.addObject("user", curUser);
        modelAndView.addObject("totalApplications", activeApplications.stream().count());
        modelAndView.addObject("applications", activeApplications);

        modelAndView.setViewName("recruiter-applications");
        return modelAndView;
    }

    @PutMapping("/recruiter-applications/{id}")
    public String setStatusApplication(@PathVariable UUID id, @RequestParam String status) {

        applicationService.updateStatusOnApplication(id, status);

        return "redirect:/recruiter-applications";
    }

    @DeleteMapping("/recruiter-applications/job/{jobId}/delete")
    public String deleteJob(@PathVariable UUID jobId) {
        jobService.softDeleteJob(jobId);
        return "redirect:/recruiter-applications";
    }

    @GetMapping("/applications/{id}/cv")
    public ResponseEntity<byte[]> viewCv(@PathVariable UUID id) {

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        byte[] file = application.getCvFile();
        if (file == null || file.length == 0) {

            return ResponseEntity.notFound().build();
        }

        String contentType = application.getCvContentType();
        if (contentType == null || contentType.isBlank()) {
            contentType = MediaType.APPLICATION_PDF_VALUE; // по подразбиране
        }

        String fileName = application.getCvFileName();
        if (fileName == null || fileName.isBlank()) {
            fileName = "cv.pdf";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }
}