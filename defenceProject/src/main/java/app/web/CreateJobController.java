package app.web;

import jakarta.validation.Valid;
import app.security.UserData;
import app.service.JobService;
//import main.web.dto.CreateJobRequest;
import app.web.dto.CreateJobRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasRole('RECRUITER')")
public class CreateJobController {

  private final JobService jobService;

  public CreateJobController(JobService jobService) {
    this.jobService = jobService;
  }

  @GetMapping("/recruiter/jobs/new")
  public ModelAndView createJob(@AuthenticationPrincipal UserData userData, @ModelAttribute("createJobRequest") CreateJobRequest createJobRequest) {

    ModelAndView modelAndView = new ModelAndView("recruiter-create-job-steps");
    modelAndView.addObject("user", userData);
    return modelAndView;
  }


  @PostMapping("/recruiter/jobs")
  public ModelAndView submitJob(@AuthenticationPrincipal UserData userData,
                                @Valid @ModelAttribute("createJobRequest") CreateJobRequest createJobRequest,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      ModelAndView modelAndView = new ModelAndView("recruiter-create-job-steps");
      modelAndView.addObject("user", userData);
      return modelAndView;
    }

    jobService.createJob(createJobRequest, userData);
    redirectAttributes.addFlashAttribute("jobCreatedTitle", createJobRequest.getTitle());

    return new ModelAndView("redirect:/recruiter/jobs/new");
  }
}
