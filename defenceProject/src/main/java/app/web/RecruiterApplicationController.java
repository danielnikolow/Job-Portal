package app.web;

import app.model.Application;
import app.model.User;
import app.repository.ApplicationRepository;
import app.repository.UserRepository;
import app.security.UserData;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
public class RecruiterApplicationController {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationService applicationService;


    public RecruiterApplicationController(UserRepository userRepository, ApplicationRepository applicationRepository, ApplicationService applicationService) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.applicationService = applicationService;
    }

    @GetMapping("/recruiter-applications")
    public ModelAndView getRecruiterApplicationPage(@AuthenticationPrincipal UserData userData) {

        ModelAndView modelAndView = new ModelAndView();
        Optional<User> user = userRepository.findById(userData.getUserId());
        User curUser = user.get();

        modelAndView.addObject("user", curUser);
        modelAndView.addObject("totalApplications", applicationRepository.findAll().stream().count());
        modelAndView.addObject("applications", applicationRepository.findAll());

        modelAndView.setViewName("recruiter-applications");
        return modelAndView;
    }

    @PutMapping("/recruiter-applications/{id}")
    public String setStatusApplication(@PathVariable UUID id, @RequestParam String status) {

        applicationService.updateStatusOnApplication(id, status);

        return "redirect:/recruiter-applications";
    }
}