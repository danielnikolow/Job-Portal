package app.service;

import app.model.Job;
import app.model.JobAlert;
import app.model.WorkMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobMatchingService {

    public int score(Job job, JobAlert alert) {
        int score = 0;
        int maxPossible = 0;

        // 1) Позиция / заглавие (до 30 точки)
        if (alert.getDesiredPosition() != null && !alert.getDesiredPosition().trim().isEmpty()) {
            maxPossible += 30;
            if (positionMatches(job, alert)) {
                score += 30;
            }
        }

        // 2) Умения (до 40 точки)
        int skillsPoints = skillsScore(job, alert);
        score += skillsPoints;
        if (alert.getRequiredSkills() != null && !alert.getRequiredSkills().isEmpty()) {
            maxPossible += 40;
        }

        // 3) Локация / work mode (до 10 точки)
        if (alert.getWorkMode() != null || (alert.getLocation() != null && !alert.getLocation().trim().isEmpty())) {
            maxPossible += 10;
            if (locationMatches(job, alert)) {
                score += 10;
            }
        }

        // 4) Заплата (до 10 точки)
        if (alert.getMinSalary() != null && alert.getMinSalary() > 0) {
            maxPossible += 10;
            if (salaryMatches(job, alert)) {
                score += 10;
            }
        }

        // 5) Ниво (до 10 точки) - пропускаме, тъй като Job няма experienceLevel
        // Ако няма критерии, даваме базов score
        if (maxPossible == 0) {
            score = 50;
        }

        return Math.min(score, 100);
    }

    public boolean isMatch(Job job, JobAlert alert) {
        int score = score(job, alert);
        return score >= alert.getMinScore();
    }

    private boolean positionMatches(Job job, JobAlert alert) {
        if (alert.getDesiredPosition() == null || alert.getDesiredPosition().trim().isEmpty()) {
            return false;
        }
        if (job.getTitle() == null || job.getTitle().trim().isEmpty()) {
            return false;
        }
        String jobTitle = job.getTitle().toLowerCase();
        String desiredPosition = alert.getDesiredPosition().toLowerCase();
        if (jobTitle.contains(desiredPosition) || desiredPosition.contains(jobTitle)) {
            return true;
        }
        String[] keywords = desiredPosition.split("\\s+");
        int matches = 0;
        for (String keyword : keywords) {
            if (keyword.length() > 3 && jobTitle.contains(keyword)) {
                matches++;
            }
        }
        return matches >= Math.max(1, keywords.length / 2);
    }

    private int skillsScore(Job job, JobAlert alert) {
        if (alert.getRequiredSkills() == null || alert.getRequiredSkills().isEmpty()) {
            return 0;
        }
        String jobText = ((job.getDescription() != null ? job.getDescription() : "") + 
                          " " + (job.getRequirements() != null ? job.getRequirements() : "") +
                          " " + (job.getSummary() != null ? job.getSummary() : "")).toLowerCase();
        
        List<String> requiredSkills = alert.getRequiredSkills().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        int matchedSkills = 0;
        for (String requiredSkill : requiredSkills) {
            if (jobText.contains(requiredSkill.toLowerCase())) {
                matchedSkills++;
            }
        }

        if (requiredSkills.isEmpty()) {
            return 0;
        }
        return (int) ((double) matchedSkills / requiredSkills.size() * 40);
    }

    private boolean locationMatches(Job job, JobAlert alert) {
        if (alert.getWorkMode() != null && job.getWorkModel() != null) {
            String jobWorkModel = job.getWorkModel().toUpperCase();
            if (alert.getWorkMode().name().equals(jobWorkModel) || 
                (alert.getWorkMode() == WorkMode.REMOTE && jobWorkModel.contains("REMOTE")) ||
                (alert.getWorkMode() == WorkMode.HYBRID && jobWorkModel.contains("HYBRID"))) {
                return true;
            }
        }
        if (alert.getLocation() != null && !alert.getLocation().trim().isEmpty()) {
            if (job.getLocation() != null) {
                String alertLocation = alert.getLocation().toLowerCase();
                String jobLocation = job.getLocation().toLowerCase();
                if (jobLocation.contains(alertLocation) || alertLocation.contains(jobLocation)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean salaryMatches(Job job, JobAlert alert) {
        if (alert.getMinSalary() == null || alert.getMinSalary() <= 0) {
            return false;
        }
        if (job.getSalaryMin() != null && job.getSalaryMin().intValue() >= alert.getMinSalary()) {
            return true;
        }
        if (job.getSalaryMax() != null && job.getSalaryMax().intValue() >= alert.getMinSalary()) {
            return true;
        }
        return false;
    }
}

