package app.service;

import app.model.JobAlert;
import app.repository.JobAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final JobAlertRepository jobAlertRepository;

    public JobAlert createAlert(JobAlert alert) {
        validateAlert(alert);
        return jobAlertRepository.save(alert);
    }

    public Optional<JobAlert> getAlertById(UUID alertId) {
        return jobAlertRepository.findById(alertId);
    }

    public List<JobAlert> getUserAlerts(UUID userId) {
        return jobAlertRepository.findByUserId(userId);
    }

    public List<JobAlert> getActiveUserAlerts(UUID userId) {
        return jobAlertRepository.findByUserIdAndActiveTrue(userId);
    }

    @Transactional
    public void deleteAlert(UUID alertId) {
        jobAlertRepository.deleteById(alertId);
    }

    @Transactional
    public void toggleActive(UUID alertId) {
        JobAlert alert = jobAlertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found: " + alertId));
        alert.setActive(!alert.isActive());
        jobAlertRepository.save(alert);
    }

    private void validateAlert(JobAlert alert) {
        if (alert.getMinScore() < 0 || alert.getMinScore() > 100) {
            throw new IllegalArgumentException("MinScore must be between 0 and 100");
        }
        if (alert.getMinSalary() != null && alert.getMinSalary() < 0) {
            throw new IllegalArgumentException("MinSalary must be >= 0");
        }
    }

    public List<String> parseSkills(String skillsString) {
        if (skillsString == null || skillsString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(skillsString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}

