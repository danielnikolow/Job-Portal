package app.service;

import app.repository.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import app.model.Application;
import app.model.EmploymentType;
import app.model.Job;
import app.model.User;
import app.repository.JobRepository;
import app.repository.UserRepository;
import app.security.UserData;
import app.web.dto.CreateJobRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final MatchingScheduler matchingScheduler;

    public JobService(JobRepository jobRepository, UserRepository userRepository, 
                     ApplicationRepository applicationRepository, MatchingScheduler matchingScheduler) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.matchingScheduler = matchingScheduler;
    }

    public void appliedOffer(UUID id, UserData userData, MultipartFile cvFile) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));

        User user = userRepository.findById(userData.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        User creator = userRepository.findById(job.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("Creator not found with id: " + id));

        job.setAppliedUser(userData.getUserId());

        Application application = new Application();
        application.setJob(job);
        application.setUser(user);
        application.setSubmittedOn(LocalDate.from(LocalDateTime.now()));

        try {
            application.setCvFile(cvFile.getBytes());
            application.setCvFileName(cvFile.getOriginalFilename());
            application.setCvContentType(cvFile.getContentType());
            application.setActive(true);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CV file", e);
        }

        applicationRepository.save(application);

        userRepository.save(user);
        userRepository.save(creator);
    }

    public List<Job> searchOffers(String keyword, String location, EmploymentType employmentType) {

        String normalizedKeyword = StringUtils.hasText(keyword) ? keyword.trim() : null;
        String normalizedLocation = StringUtils.hasText(location) ? location.trim() : null;
        return jobRepository.searchOffers(normalizedKeyword, normalizedLocation, employmentType);

    }

    public void createJob(CreateJobRequest request, UserData userData) {
        Job job = Job.builder()
                .title(request.getTitle())
                .team(request.getTeam())
                .company(request.getCompany())
                .location(request.getLocation())
                .employmentType(EmploymentType.valueOf(request.getEmploymentType()))
                .workModel(request.getWorkModel())
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .currency(request.getCurrency())
                .payCycle(request.getPayCycle())
                .summary(request.getSummary())
                .description(request.getDescription())
                .requirements(request.getRequirements())
                .publishDate(request.getPublishDate())
                .deadline(request.getDeadline())
                .creatorId(userData.getUserId())
                .active(true)
                .build();

        job = jobRepository.save(job);

        try {
            checkJobAlertsAndCreateMatches(job);
        } catch (Exception e) {
            log.error("Error checking job alerts for job: " + job.getId(), e);
        }
    }

    public EmploymentType resolveEmploymentType(String jobType) {
        if (!StringUtils.hasText(jobType)) {
            return null;
        }
        try {
            return EmploymentType.valueOf(jobType.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private void checkJobAlertsAndCreateMatches(Job job) {
        log.info("Checking job alerts for newly created job: {}", job.getId());
        try {
            matchingScheduler.matchJobAgainstAlerts(job);
        } catch (Exception e) {
            log.error("Error matching job {} against alerts: {}", job.getId(), e.getMessage(), e);
        }
    }

    public void softDeleteJob(UUID jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + jobId));

        job.setActive(false);
        jobRepository.save(job);

        List<Application> applications = applicationRepository.findAll().stream()
                .filter(app -> app.getJob() != null && app.getJob().getId().equals(jobId))
                .toList();

        for (Application application : applications) {
            application.setStatus("job delete");
            applicationRepository.save(application);
        }
    }
}

