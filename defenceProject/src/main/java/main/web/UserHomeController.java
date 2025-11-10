package main.web;

import main.model.Offer;
import main.repository.OfferRepository;
import main.security.UserData;
import main.service.OfferService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.UUID;


@Controller
public class UserHomeController {

    private final OfferRepository offerRepository;
    private final OfferService offerService;

    public UserHomeController(OfferRepository offerRepository, OfferService offerService) {
        this.offerRepository = offerRepository;
        this.offerService = offerService;
    }

    @GetMapping("/user-home")
    public ModelAndView userHome(@AuthenticationPrincipal UserData userData) {

        List<Offer> latestJobs = offerRepository.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-home");
        modelAndView.addObject("user", userData);
        modelAndView.addObject("latestJobs", latestJobs);

        return modelAndView;
    }

    @PatchMapping("/jobs/{id}")
    public String appliedOffer(@PathVariable UUID id, @AuthenticationPrincipal UserData userData) {

        offerService.appliedOffer(id, userData);

        return "redirect:/user-home";

    }
}
