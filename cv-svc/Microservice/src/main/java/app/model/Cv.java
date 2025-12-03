package app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cvs")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cv {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Step 1: Основни данни
    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 100)
    private String birthDate;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String location;

    @Column(length = 200)
    private String linkedInUrl;

    @Column(length = 1000)
    private String summary;

    // Step 2: Опит
    @Column(length = 100)
    private String jobTitle;

    @Column(length = 100)
    private String company;

    @Column(name = "experience_start_date")
    private LocalDate experienceStartDate;

    @Column(name = "experience_end_date")
    private LocalDate experienceEndDate;

    @Column(name = "experience_description", length = 2000)
    private String experienceDescription;

    // Step 3: Образование
    @Column(length = 100)
    private String degree;

    @Column(length = 100)
    private String school;

    @Column(name = "education_start_date")
    private LocalDate educationStartDate;

    @Column(name = "education_end_date")
    private LocalDate educationEndDate;

    @Column(name = "education_info", length = 1000)
    private String educationInfo;

    // Step 4: Умения
    @Column(name = "technical_skills", length = 500)
    private String technicalSkills;

    @Column(length = 500)
    private String languages;

    @Column(nullable = false)
    private UUID userId;

    @Column(name = "created_at")
    private LocalDate createdAt;

    private String cvName;

}