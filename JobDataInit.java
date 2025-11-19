package main.service;
import main.model.EmploymentType;
import main.model.Job;
import main.model.User;
import main.repository.JobRepository;
import main.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Configuration
@Profile("dev")
public class JobDataInit {

    private final UUID id = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private UserRepository userRepository;

    public JobDataInit(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    CommandLineRunner seedOffers(JobRepository jobRepository) {

        return args -> {

//            String firstName = "R";
//            User recruiter = userRepository.findByFirstName(firstName);
//            UUID creatorId = recruiter.getId();

            if (jobRepository.count() > 0) return;

            Job o1 = new Job();
            o1.setTitle("Java Developer (Spring Boot)");
            o1.setDescription("""
                    Търсим Java разработчик със Spring Boot 3, JPA, REST.
                    Опит с Docker и CI/CD е предимство.
                    """);
            o1.setSalaryMin(new BigDecimal("4200.00"));
            o1.setSalaryMax(new BigDecimal("6500.00"));
            o1.setEmploymentType(EmploymentType.FULL_TIME);
            o1.setLocation("Sofia (Hybrid)");
            o1.setCompany("TechWave");
            o1.setActive(true);
            o1.setCreatorId(id);

            Job o2 = new Job();
            o2.setTitle("Junior Java Engineer (Microservices)");
            o2.setDescription("""
                    Идеално за начинаещи: работа по микроуслуги, Feign, OpenAPI, тестове.
                    Менторство и бюджет за обучение.
                    """);
            o2.setSalaryMin(new BigDecimal("2800.00"));
            o2.setSalaryMax(new BigDecimal("3800.00"));
            o2.setEmploymentType(EmploymentType.FULL_TIME);
            o2.setLocation("Remote");
            o2.setCompany("CloudForge");
            o2.setActive(true);
            o2.setCreatorId(id);

            Job o3 = new Job();
            o3.setTitle("Part-time Java / Kotlin Intern");
            o3.setDescription("Стаж 20ч/седмица. Работа по малки REST услуги и unit тестове.");
            o3.setSalaryMin(new BigDecimal("1200.00"));
            o3.setSalaryMax(new BigDecimal("1600.00"));
            o3.setEmploymentType(EmploymentType.INTERNSHIP);
            o3.setLocation("Sofia");
            o3.setCompany("StartUpHub");
            o3.setActive(true);
            o3.setCreatorId(id);

            Job o4 = new Job();
            o4.setTitle("Contract Java Engineer (6 months)");
            o4.setDescription("Контракт по проект: миграция към Spring Boot 3.4, кеширане и наблюдаемост.");
            o4.setSalaryMin(new BigDecimal("7000.00"));
            o4.setSalaryMax(new BigDecimal("9000.00"));
            o4.setEmploymentType(EmploymentType.CONTRACT);
            o4.setLocation("Remote (EU)");
            o4.setCompany("DataBridge");
            o4.setActive(true);
            o4.setCreatorId(id);

            Job o5 = new Job();
            o5.setTitle("Contract Java Engineer (6 months)");
            o5.setDescription("Контракт по проект: миграция към Spring Boot 3.4, кеширане и наблюдаемост.");
            o5.setSalaryMin(new BigDecimal("7000.00"));
            o5.setSalaryMax(new BigDecimal("9000.00"));
            o5.setEmploymentType(EmploymentType.CONTRACT);
            o5.setLocation("Remote (EU)");
            o5.setCompany("DataBridge");
            o5.setActive(true);
            o5.setCreatorId(id);

            Job o6 = new Job();
            o6.setTitle("Contract Java Engineer (6 months)");
            o6.setDescription("Контракт по проект: миграция към Spring Boot 3.4, кеширане и наблюдаемост.");
            o6.setSalaryMin(new BigDecimal("7000.00"));
            o6.setSalaryMax(new BigDecimal("9000.00"));
            o6.setEmploymentType(EmploymentType.CONTRACT);
            o6.setLocation("Remote (EU)");
            o6.setCompany("DataBridge");
            o6.setActive(true);
            o6.setCreatorId(id);

            Job o7 = new Job();
            o7.setTitle("Contract Java Engineer (6 months)");
            o7.setDescription("Контракт по проект: миграция към Spring Boot 3.4, кеширане и наблюдаемост.");
            o7.setSalaryMin(new BigDecimal("7000.00"));
            o7.setSalaryMax(new BigDecimal("9000.00"));
            o7.setEmploymentType(EmploymentType.CONTRACT);
            o7.setLocation("Remote (EU)");
            o7.setCompany("DataBridge");
            o7.setActive(true);
            o7.setCreatorId(id);

            jobRepository.saveAll(List.of(o1, o2, o3, o4, o5, o6, o7));
        };
    }
}
