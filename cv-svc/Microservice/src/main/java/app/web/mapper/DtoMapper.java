package app.web.mapper;
import app.model.Cv;
import app.web.dto.CvResponse;
import lombok.experimental.UtilityClass;
import java.time.LocalDate;

@UtilityClass
public class DtoMapper {

    public static CvResponse fromCv(Cv cv) {

        return CvResponse.builder()
                .id(cv.getId())
                .fullName(cv.getFullName())
                .birthDate(cv.getBirthDate())
                .email(cv.getEmail())
                .phone(cv.getPhone())
                .location(cv.getLocation())
                .linkedInUrl(cv.getLinkedInUrl())
                .summary(cv.getSummary())

                .jobTitle(cv.getJobTitle())
                .company(cv.getCompany())
                .experienceStartDate(cv.getExperienceStartDate())
                .experienceEndDate(cv.getExperienceEndDate())
                .experienceDescription(cv.getExperienceDescription())

                .degree(cv.getDegree())
                .school(cv.getSchool())
                .educationStartDate(cv.getEducationStartDate())
                .educationEndDate(cv.getEducationEndDate())
                .educationInfo(cv.getEducationInfo())

                .technicalSkills(cv.getTechnicalSkills())
                .languages(cv.getLanguages())

                .userId(cv.getUserId())
                .cvName(cv.getCvName())
                .createdAt(LocalDate.now())
                .build();
    }
}
