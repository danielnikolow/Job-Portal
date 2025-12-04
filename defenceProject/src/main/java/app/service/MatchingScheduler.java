package app.service;

import app.model.*;
import app.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingScheduler {

    private final JobRepository jobRepository;
    private final JobAlertRepository jobAlertRepository;
    private final JobMatchRepository jobMatchRepository;
    private final JobMatchingService matchingService;
    private final NotificationPlanner notificationPlanner;

    @Scheduled(fixedRate = 300000) // На 5 минути
    @Transactional
    public void runMatching() {
        log.info("Starting job matching process...");
        List<Job> activeJobs = jobRepository.findAll().stream()
                .filter(Job::isActive)
                .toList();
        List<JobAlert> alerts = jobAlertRepository.findByActiveTrue();
        log.info("Found {} active jobs and {} active alerts", activeJobs.size(), alerts.size());

        int matchesCreated = 0;
        for (Job job : activeJobs) {
            for (JobAlert alert : alerts) {
                try {
                    int score = matchingService.score(job, alert);
                    if (score >= alert.getMinScore()) {
                        boolean alreadyMatched = jobMatchRepository.existsByUserIdAndJobId(alert.getUserId(), job.getId());
                        if (!alreadyMatched) {
                            JobMatch match = JobMatch.builder()
                                    .userId(alert.getUserId())
                                    .jobId(job.getId())
                                    .alertId(alert.getId())
                                    .score(score)
                                    .matchedAt(Instant.now())
                                    .build();
                            jobMatchRepository.save(match);
                            matchesCreated++;
                            notificationPlanner.planNotification(alert, job, match);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error matching job {} with alert {}: {}", job.getId(), alert.getId(), e.getMessage());
                }
            }
        }
        log.info("Matching completed. Created {} matches", matchesCreated);
    }

    @Transactional
    public void runMatchingNow() {
        log.info("Manually triggering matching process...");
        runMatching();
    }

    @Transactional
    public void matchJobAgainstAlerts(Job job) {
        log.info("Matching new job '{}' against active alerts", job.getTitle());
        if (!job.isActive()) {
            log.debug("Job {} is not active, skipping matching", job.getId());
            return;
        }

        List<JobAlert> alerts = jobAlertRepository.findByActiveTrue();
        log.info("Found {} active alerts to check against job {}", alerts.size(), job.getId());

        int matchesCreated = 0;
        for (JobAlert alert : alerts) {
            try {
                int score = matchingService.score(job, alert);
                log.debug("Job '{}' vs Alert '{}' (user: {}): score = {} (min required: {})",
                        job.getTitle(),
                        alert.getDesiredPosition() != null ? alert.getDesiredPosition() : "N/A",
                        alert.getUserId(),
                        score,
                        alert.getMinScore());

                if (score >= alert.getMinScore()) {
                    boolean alreadyMatched = jobMatchRepository.existsByUserIdAndJobId(alert.getUserId(), job.getId());
                    if (!alreadyMatched) {
                        JobMatch match = JobMatch.builder()
                                .userId(alert.getUserId())
                                .jobId(job.getId())
                                .alertId(alert.getId())
                                .score(score)
                                .matchedAt(Instant.now())
                                .build();
                        jobMatchRepository.save(match);
                        matchesCreated++;
                        log.info("Created match: job '{}' matched with alert '{}' (score: {})",
                                job.getTitle(), alert.getDesiredPosition(), score);
                        notificationPlanner.planNotification(alert, job, match);
                    } else {
                        log.debug("Match already exists for job '{}' and user {}", job.getTitle(), alert.getUserId());
                    }
                } else {
                    log.debug("Score {} below threshold {} for job '{}' and alert '{}'",
                            score, alert.getMinScore(), job.getTitle(), alert.getDesiredPosition());
                }
            } catch (Exception e) {
                log.error("Error matching job {} with alert {}: {}",
                        job.getId(), alert.getId(), e.getMessage(), e);
            }
        }
        log.info("Matching completed for job {}. Created {} matches", job.getId(), matchesCreated);
    }
}

