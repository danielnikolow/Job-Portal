package app.service;

import app.model.*;
import app.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPlanner {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void planNotification(JobAlert alert, Job job, JobMatch match) {
        if (alert.getFrequency() == NotifyFrequency.INSTANT) {
            sendInstant(alert, job, match);
        }
        // DAILY/WEEKLY digest може да се добави по-късно
    }

    private void sendInstant(JobAlert alert, Job job, JobMatch match) {
        UUID userId = alert.getUserId();
        Notification notification = Notification.builder()
                .userId(userId)
                .type("JOB_MATCH")
                .title("Нова подходяща обява: " + job.getTitle())
                .message(String.format("Намерихме обява, която отговаря на вашите критерии (score: %d/100).", match.getScore()))
                .linkUrl("/job/" + job.getId())
                .read(false)
                .createdAt(Instant.now())
                .build();
        notificationRepository.save(notification);
        log.info("Created notification for user {}: job '{}' (score: {})", userId, job.getTitle(), match.getScore());
    }
}

