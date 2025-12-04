package app.web;
import app.model.Application;
import app.model.User;
import app.repository.ApplicationRepository;
import app.repository.UserRepository;
import app.security.UserData;
import app.service.ApplicationService;
import app.service.JobService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserApplicaitonsController {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationService applicationService;

    public UserApplicaitonsController(UserRepository userRepository, ApplicationRepository applicationRepository, ApplicationService applicationService) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.applicationService = applicationService;
    }

    @GetMapping("/user-applications")
    public ModelAndView getApplications(@AuthenticationPrincipal UserData userData) {

        Optional<User> user = userRepository.findById(userData.getUserId());
        User curUser = user.get();

        List<Application> applications = curUser.getApplications()
                .stream().filter(app -> app.isActive())
                .toList();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("totalApplications", applications.stream().count());
        modelAndView.addObject("applications", applications);


        modelAndView.setViewName("user-applications");
        return modelAndView;
    }

    @DeleteMapping("/user-applications/app/{appId}/delete")
    public String deleteApplication(@PathVariable UUID appId) {
        applicationService.softDeleteApplication(appId);
        return "redirect:/user-applications";
    }
}
