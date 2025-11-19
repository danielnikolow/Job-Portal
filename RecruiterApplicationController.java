package main.web;

import main.model.Application;
import main.model.User;
import main.repository.UserRepository;
import main.security.UserData;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.Optional;

@Controller
public class RecruiterApplicationController {

    private final UserRepository userRepository;

    public RecruiterApplicationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/recruiter-applications")
    public ModelAndView getRecruiterApplicationPage(@AuthenticationPrincipal UserData userData) {

        ModelAndView modelAndView = new ModelAndView();
        Optional<User> user = userRepository.findById(userData.getUserId());
        int totalApplications = user.get().getApplications().size();

        modelAndView.addObject("totalApplications", totalApplications);

        modelAndView.setViewName("recruiter-applications.html");

        return modelAndView;
    }
}