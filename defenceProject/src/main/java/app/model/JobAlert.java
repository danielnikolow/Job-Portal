package app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_alerts")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "desired_position", length = 200)
    private String desiredPosition;

    @ElementCollection
    @CollectionTable(name = "job_alert_required_skills", joinColumns = @JoinColumn(name = "job_alert_id"))
    @Column(name = "skill")
    @Builder.Default
    private List<String> requiredSkills = new ArrayList<>();

    @Column(length = 200)
    private String location;

    @Column(name = "min_salary")
    private Integer minSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level")
    private ExperienceLevel experienceLevel;

    @Column(name = "employment_type", length = 50)
    private String employmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_mode")
    private WorkMode workMode;

    @Column(name = "min_score", nullable = false)
    @Min(0)
    @Max(100)
    @Builder.Default
    private Integer minScore = 60;

    @Enumerated(EnumType.STRING)
    @Column(name = "notify_channel", nullable = false)
    @Builder.Default
    private NotifyChannel notifyChannel = NotifyChannel.IN_APP;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    @Builder.Default
    private NotifyFrequency frequency = NotifyFrequency.INSTANT;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}

