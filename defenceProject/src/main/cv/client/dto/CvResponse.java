package app.cv.client.dto;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class CvResponse {

    private UUID id;

    private String fullName;

    private String birthDate;

    private String email;

    private String phone;

    private String location;

    private String linkedInUrl;

    private String summary;

    private String jobTitle;

    private String company;

    private LocalDate experienceStartDate;
    private LocalDate experienceEndDate;

    private String experienceDescription;

    private String degree;

    private String school;

    private LocalDate educationStartDate;
    private LocalDate educationEndDate;

    private String educationInfo;

    private String technicalSkills;

    private String languages;

    private String cvName;

    private UUID userId;

    private LocalDate createdAt;
}
