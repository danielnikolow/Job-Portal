package app.web;

import app.model.Job;
import app.repository.JobRepository;
import app.security.UserData;
import app.service.JobService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.util.Optional;
import java.util.UUID;


@Controller
public class JobController {

    private final JobRepository jobRepository;
    private final JobService jobService;

    public JobController(JobRepository jobRepository, JobService jobService) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }

    @GetMapping("/job/{id}")
    public ModelAndView showFullJob(@PathVariable("id") UUID jobId, @AuthenticationPrincipal UserData userData) {

        Optional<Job> job = jobRepository.findById(jobId);
        Job curJob = job.get();

        ModelAndView modelAndView = new ModelAndView("job-details-new");
        modelAndView.addObject("user", userData);
        modelAndView.addObject("job", curJob);
        
        return modelAndView;
    }

    @PutMapping("/appliedJob/{id}")
    public ModelAndView appliedJob(@PathVariable("id") UUID jobId, @AuthenticationPrincipal UserData userData,  @RequestParam("cvFile") MultipartFile cvFile) {

        Optional<Job> job = jobRepository.findById(jobId);
        Job curJob = job.get();

        ModelAndView modelAndView = new ModelAndView("job-details-new");
        modelAndView.addObject("user", userData);
        modelAndView.addObject("job", curJob);

        if (!MediaType.APPLICATION_PDF_VALUE.equals(cvFile.getContentType())) {
            return modelAndView;
        }

        jobService.appliedOffer(jobId, userData, cvFile);
        return new ModelAndView( "redirect:/job/" + jobId);
    }
}