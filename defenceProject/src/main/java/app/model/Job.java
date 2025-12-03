package app.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private String team;

    private String company;

    private String location;

    @Enumerated
    private EmploymentType employmentType;

    private String workModel;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

    private String currency;

    private String payCycle;

    private LocalDate publishDate;

    private boolean active;

    private UUID appliedUser;

    private String summary;

    private String description;

    private String requirements;

    private LocalDate postedOn;

    private LocalDate deadline;

    @OneToMany(mappedBy = "job")
    private List<Application> applications;

    @Column(nullable = false)
    private UUID creatorId;

}
