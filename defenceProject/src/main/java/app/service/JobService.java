package main.service;

import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import main.model.Application;
import main.model.EmploymentType;
import main.model.Job;
import main.model.User;
import main.repository.JobRepository;
import main.repository.UserRepository;
import main.security.UserData;
import main.web.dto.CreateJobRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationService applicationService;

    public JobService(JobRepository jobRepository, ApplicationService applicationService, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationService = applicationService;
    }

    public void appliedOffer(UUID id, UserData userData) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));

        User user = userRepository.findById(userData.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        User creator = userRepository.findById(job.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("Creator not found with id: " + id));

        job.setAppliedUser(userData.getUserId());

        Application application = applicationService.createApplication(job, creator);

        user.getApplications().add(application);
        creator.getApplications().add(application);

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
                .build();

        jobRepository.save(job);
    }
}

