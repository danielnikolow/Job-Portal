package app.web;

import app.model.EmploymentType;
import app.model.Job;
import app.repository.JobRepository;
import app.security.UserData;
import app.service.JobService;
import app.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;


@Controller
public class HomeController {

    private final JobRepository jobRepository;
    private final JobService jobService;
    private final UserService userService;

    public HomeController(JobRepository jobRepository, JobService jobService, UserService userService) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
        this.userService = userService;
    }

    @GetMapping("/user-home")
    public ModelAndView userHome(@AuthenticationPrincipal UserData userData, HttpServletResponse response) {

        List<Job> allJobs = jobRepository.findAll();
        List<Job> latestJobs = allJobs.stream()
                .filter(job -> job.isActive())
                .toList();

        ModelAndView modelAndView = userService.buildUserHomeView(userData, latestJobs);

        return modelAndView;
    }

    @GetMapping("/jobs/search")
    public ModelAndView searchJobs(@RequestParam(name = "keyword", required = false) String keyword,
                                   @RequestParam(name = "location", required = false) String location,
                                   @RequestParam(name = "jobType", required = false) String jobType,
                                   @AuthenticationPrincipal UserData userData,
                                   HttpServletResponse response) {

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        if (!StringUtils.hasText(keyword) && !StringUtils.hasText(location) && !StringUtils.hasText(jobType)) {
            return new ModelAndView("redirect:/user-home");
        }

        EmploymentType employmentType = jobService.resolveEmploymentType(jobType);
        List<Job> offers = jobService.searchOffers(keyword, location, employmentType);

        ModelAndView modelAndView = userService.buildUserHomeView(userData, offers);
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("keyword", keyword);
        searchParams.put("location", location);
        searchParams.put("jobType", employmentType != null ? employmentType.name() : null);
        modelAndView.addObject("searchParams", searchParams);
        modelAndView.addObject("searchActive", true);
        modelAndView.addObject("searchResultCount", offers.size());

        return modelAndView;
    }

    @GetMapping({"/index"})
    public String index() {
        return "index";
    }
}
