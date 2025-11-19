package main.service;

import lombok.extern.slf4j.Slf4j;
import main.model.Job;
import main.model.Application;
import main.model.User;
import main.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application createApplication(Job job, User curUser) {

        Application application = Application.builder()
                .user(curUser)
                .job(job)
                .build();

        applicationRepository.save(application);
        return application;
    }
}
