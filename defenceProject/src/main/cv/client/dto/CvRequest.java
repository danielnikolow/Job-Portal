package app.cv.client.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CvRequest {

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

    private String experienceStartDate;
    private String experienceEndDate;

    private String experienceDescription;

    private String degree;

    private String school;

    private String educationStartDate;
    private String educationEndDate;

    private String educationInfo;

    private String technicalSkills;

    private String languages;

    private String cvName;

    private UUID userId;

    private LocalDate createdAt;
}