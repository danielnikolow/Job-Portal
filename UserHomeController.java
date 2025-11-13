package main.web;

import main.model.EmploymentType;
import main.model.Offer;
import main.repository.OfferRepository;
import main.security.UserData;
import main.service.OfferService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return buildUserHomeView(userData, latestJobs);
    }

    @GetMapping("/jobs/search")
    public ModelAndView searchJobs(@RequestParam(name = "keyword", required = false) String keyword,
                                   @RequestParam(name = "location", required = false) String location,
                                   @RequestParam(name = "jobType", required = false) String jobType,
                                   @AuthenticationPrincipal UserData userData) {

        if (!StringUtils.hasText(keyword) && !StringUtils.hasText(location) && !StringUtils.hasText(jobType)) {
            return new ModelAndView("redirect:/user-home");
        }

        EmploymentType employmentType = resolveEmploymentType(jobType);
        List<Offer> offers = offerService.searchOffers(keyword, location, employmentType);

        ModelAndView modelAndView = buildUserHomeView(userData, offers);
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("keyword", keyword);
        searchParams.put("location", location);
        searchParams.put("jobType", employmentType != null ? employmentType.name() : null);
        modelAndView.addObject("searchParams", searchParams);
        modelAndView.addObject("searchActive", true);
        modelAndView.addObject("searchResultCount", offers.size());

        return modelAndView;
    }

    @PatchMapping("/jobs/{id}")
    public String appliedOffer(@PathVariable UUID id, @AuthenticationPrincipal UserData userData) {
        offerService.appliedOffer(id, userData);
        return "redirect:/user-home";
    }

    private ModelAndView buildUserHomeView(UserData userData, List<Offer> offers) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-home");
        modelAndView.addObject("user", userData);
        modelAndView.addObject("latestJobs", offers);

        Map<String, Object> defaultParams = new HashMap<>();
        defaultParams.put("keyword", null);
        defaultParams.put("location", null);
        defaultParams.put("jobType", null);

        modelAndView.addObject("searchParams", defaultParams);
        modelAndView.addObject("searchActive", false);
        modelAndView.addObject("searchResultCount", offers.size());
        return modelAndView;
    }

    private EmploymentType resolveEmploymentType(String jobType) {
        if (!StringUtils.hasText(jobType)) {
            return null;
        }

        try {
            return EmploymentType.valueOf(jobType.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
