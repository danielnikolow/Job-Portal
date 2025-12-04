package app.web.dto;

import app.model.ExperienceLevel;
import app.model.NotifyChannel;
import app.model.NotifyFrequency;
import app.model.WorkMode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CreateJobAlertRequest {
    private String desiredPosition;
    private String requiredSkills;
    private String location;
    private Integer minSalary;
    private ExperienceLevel experienceLevel;
    private String employmentType;
    private WorkMode workMode;
    
    @Min(0)
    @Max(100)
    private Integer minScore = 60;
    
    private NotifyChannel notifyChannel = NotifyChannel.IN_APP;
    private NotifyFrequency frequency = NotifyFrequency.INSTANT;
}

