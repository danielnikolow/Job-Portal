package app.service;

import lombok.extern.slf4j.Slf4j;
import app.model.Job;
import app.model.Application;
import app.model.User;
import app.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
                .submittedOn(LocalDate.now())
                .build();

        applicationRepository.save(application);
        return application;
    }

        public void updateStatusOnApplication(UUID id, String status) {

        Optional<Application> application = applicationRepository.findById(id);
        User user = application.get().getUser();

        List<Application> userApplications = user.getApplications();

        Optional<Application> foundApplication = userApplications.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();

        Application application2 = foundApplication.get();
        application2.setStatus(status);

        applicationRepository.save(application2);
    }
}
