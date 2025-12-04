package app.web;

import app.model.JobAlert;
import app.model.Notification;
import app.repository.NotificationRepository;
import app.security.UserData;
import app.service.AlertService;
import app.service.MatchingScheduler;
import app.web.dto.CreateJobAlertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/job-alerts")
public class JobAlertController {

    private final AlertService alertService;
    private final NotificationRepository notificationRepository;
    private final MatchingScheduler matchingScheduler;

    public JobAlertController(AlertService alertService, NotificationRepository notificationRepository, MatchingScheduler matchingScheduler) {
        this.alertService = alertService;
        this.notificationRepository = notificationRepository;
        this.matchingScheduler = matchingScheduler;
    }

    @GetMapping
    public String listAlerts(@AuthenticationPrincipal UserData userData, Model model) {
        List<JobAlert> alerts = alertService.getUserAlerts(userData.getUserId());
        model.addAttribute("alerts", alerts);
        return "job-alerts/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("alertRequest", new CreateJobAlertRequest());
        return "job-alerts/create";
    }

    @PostMapping("/create")
    public String createAlert(@AuthenticationPrincipal UserData userData,
                             @ModelAttribute CreateJobAlertRequest request,
                             RedirectAttributes redirectAttributes) {
        try {
            List<String> skills = alertService.parseSkills(request.getRequiredSkills());
            JobAlert alert = JobAlert.builder()
                    .userId(userData.getUserId())
                    .desiredPosition(request.getDesiredPosition())
                    .requiredSkills(skills)
                    .location(request.getLocation())
                    .minSalary(request.getMinSalary())
                    .experienceLevel(request.getExperienceLevel())
                    .employmentType(request.getEmploymentType())
                    .workMode(request.getWorkMode())
                    .minScore(request.getMinScore())
                    .notifyChannel(request.getNotifyChannel())
                    .frequency(request.getFrequency())
                    .active(true)
                    .build();
            alertService.createAlert(alert);
            redirectAttributes.addFlashAttribute("success", "Job Alert създаден успешно!");
            return "redirect:/job-alerts";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Грешка при създаване: " + e.getMessage());
            return "redirect:/job-alerts/create";
        }
    }

    @PostMapping("/{id}/toggle")
    public String toggleActive(@PathVariable UUID id,
                             @AuthenticationPrincipal UserData userData,
                             RedirectAttributes redirectAttributes) {
        try {
            alertService.toggleActive(id);
            redirectAttributes.addFlashAttribute("success", "Статусът е променен!");
            return "redirect:/job-alerts";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Грешка: " + e.getMessage());
            return "redirect:/job-alerts";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteAlert(@PathVariable UUID id,
                             RedirectAttributes redirectAttributes) {
        try {
            alertService.deleteAlert(id);
            redirectAttributes.addFlashAttribute("success", "Job Alert изтрит успешно!");
            return "redirect:/job-alerts";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Грешка: " + e.getMessage());
            return "redirect:/job-alerts";
        }
    }

    @PostMapping("/trigger-matching")
    public String triggerMatching(RedirectAttributes redirectAttributes) {
        try {
            matchingScheduler.runMatchingNow();
            redirectAttributes.addFlashAttribute("success", "Matching процесът е задействан!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Грешка: " + e.getMessage());
        }
        return "redirect:/job-alerts";
    }

    @GetMapping("/notifications")
    public String listNotifications(@AuthenticationPrincipal UserData userData, Model model) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userData.getUserId());
        long unreadCount = notificationRepository.countByUserIdAndReadFalse(userData.getUserId());
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        return "job-alerts/notifications";
    }

    @PostMapping("/notifications/{id}/read")
    public String markAsRead(@PathVariable UUID id,
                            @AuthenticationPrincipal UserData userData) {
        notificationRepository.findById(id).ifPresent(notification -> {
            if (notification.getUserId().equals(userData.getUserId())) {
                notification.setRead(true);
                notificationRepository.save(notification);
            }
        });
        return "redirect:/job-alerts/notifications";
    }


}

