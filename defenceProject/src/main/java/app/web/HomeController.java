package main.web;

import jakarta.servlet.http.HttpSession;
import main.model.EmploymentType;
import main.model.Job;
import main.repository.JobRepository;
import main.security.UserData;
import main.service.JobService;
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
public class HomeController {

    private final JobRepository jobRepository;
    private final JobService jobService;

    public HomeController(JobRepository jobRepository1, JobService jobService) {
        this.jobRepository = jobRepository1;
        this.jobService = jobService;
    }

    @GetMapping("/user-home")
    public ModelAndView userHome(@AuthenticationPrincipal UserData userData) {

        List<Job> latestJobs = jobRepository.findAll();
        ModelAndView modelAndView = buildUserHomeView(userData, latestJobs);

        return modelAndView;
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
        List<Job> offers = jobService.searchOffers(keyword, location, employmentType);

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
    public String appliedJob(@PathVariable UUID id, @AuthenticationPrincipal UserData userData) {

        jobService.appliedOffer(id, userData);

        return "redirect:/user-home";
    }

//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//
//        session.invalidate();
//        return "redirect:/index";
//    }

    private ModelAndView buildUserHomeView(UserData userData, List<Job> offers) {
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
