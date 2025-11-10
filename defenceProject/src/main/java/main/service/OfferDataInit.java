package main.service;


import main.model.EmploymentType;
import main.model.Offer;
import main.repository.OfferRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Profile("dev") // стартира само при spring.profiles.active=dev
public class OfferDataInit {

    @Bean
    CommandLineRunner seedOffers(OfferRepository offerRepository/*, UserRepository userRepository*/) {
        return args -> {
            if (offerRepository.count() > 0) return;

            // Ако имаш рекрутър:
            // var recruiter = userRepository.findByUsername("recruiter")
            //       .orElseThrow(() -> new IllegalStateException("Missing recruiter user"));

            Offer o1 = new Offer();
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
            o1.setPostedOn(LocalDateTime.now().minusDays(3));
            // o1.setRecruiter(recruiter);

            Offer o2 = new Offer();
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
            o2.setPostedOn(LocalDateTime.now().minusDays(1));

            Offer o3 = new Offer();
            o3.setTitle("Part-time Java / Kotlin Intern");
            o3.setDescription("Стаж 20ч/седмица. Работа по малки REST услуги и unit тестове.");
            o3.setSalaryMin(new BigDecimal("1200.00"));
            o3.setSalaryMax(new BigDecimal("1600.00"));
            o3.setEmploymentType(EmploymentType.INTERNSHIP);
            o3.setLocation("Sofia");
            o3.setCompany("StartUpHub");
            o3.setActive(true);
            o3.setPostedOn(LocalDateTime.now().minusDays(7));

            Offer o4 = new Offer();
            o4.setTitle("Contract Java Engineer (6 months)");
            o4.setDescription("Контракт по проект: миграция към Spring Boot 3.4, кеширане и наблюдаемост.");
            o4.setSalaryMin(new BigDecimal("7000.00"));
            o4.setSalaryMax(new BigDecimal("9000.00"));
            o4.setEmploymentType(EmploymentType.CONTRACT);
            o4.setLocation("Remote (EU)");
            o4.setCompany("DataBridge");
            o4.setActive(true);
            o4.setPostedOn(LocalDateTime.now().minusDays(10));

            offerRepository.saveAll(List.of(o1, o2, o3, o4));
        };
    }
}
