package main.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 4000)
    private String description;

    @Column(precision = 12, scale = 2)
    private BigDecimal salaryMin;

    @Column(precision = 12, scale = 2)
    private BigDecimal salaryMax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EmploymentType employmentType; // FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, REMOTE

    @Column(nullable = false, length = 80)
    private String location; // Sofia / Remote / Hybrid

    @Column(nullable = false, length = 120)
    private String company;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime postedOn;

    @ManyToOne
    @JoinColumn(name = "applier_ud")
    private User appliedUser;
}
