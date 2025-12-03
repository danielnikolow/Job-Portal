package app.web;

import app.cv.client.dto.CvRequest;
import app.cv.service.CvService;
import app.repository.UserRepository;
import app.security.UserData;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class UserCreateCvController {

    private final UserRepository userRepository;
    private final CvService cvService;

    public UserCreateCvController(UserRepository userRepository, CvService cvService) {
        this.userRepository = userRepository;
        this.cvService = cvService;
    }

    @GetMapping("/create-cv")
    public ModelAndView getCreateCvPage(@AuthenticationPrincipal UserData userData, @ModelAttribute("cvRequest") CvRequest cvRequest) {

        List<CvRequest> cvs = cvService.getCvsByUserId(userData.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("create-cv");
        modelAndView.addObject("userCvs", cvs);
        modelAndView.addObject("user", userData);
        return modelAndView;
    }

    @PostMapping("/create-cv/create-new")
    public String createNewCv(@AuthenticationPrincipal UserData userData,
                              @Valid @ModelAttribute("cvRequest") CvRequest cvRequest,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.cvRequest", bindingResult);
            redirectAttributes.addFlashAttribute("cvRequest", cvRequest);
            return "redirect:/create-cv";
        }

        cvService.saveCv(cvRequest, userData.getUserId());
        redirectAttributes.addFlashAttribute("successMessage", "Ново CV е създадено успешно!");


        return "redirect:/create-cv";
    }
}