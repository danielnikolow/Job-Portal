package app.web;

import app.model.User;
import app.repository.UserRepository;
import app.security.UserData;
import app.service.UserService;
import app.web.dto.EditAccountRequest;
import app.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static jakarta.persistence.GenerationType.UUID;

@Controller
public class AccountController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AccountController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/account")
    public ModelAndView getAccountPage(@AuthenticationPrincipal UserData userData, @ModelAttribute("editAccountRequest") EditAccountRequest editAccountRequest) {

        Optional<User> user = userRepository.findById(userData.getUserId());
        User curUser = user.get();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", curUser);
        modelAndView.setViewName("account");
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView editAccount(@AuthenticationPrincipal UserData userData, @Valid @ModelAttribute("editAccountRequest") EditAccountRequest editAccountRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("account");
        }

        userService.editAccount(editAccountRequest, userData.getUserId());
        redirectAttributes.addFlashAttribute("successfulEdit", "You have edit our account successfully");

        return new ModelAndView("redirect:/account");
    }
}
