package main.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccountController {

    @GetMapping("/account")
    public ModelAndView getAccountPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("account");

        return modelAndView;
    }
}
