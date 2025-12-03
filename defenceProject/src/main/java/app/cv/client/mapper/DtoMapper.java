package app.cv.client.mapper;
import app.cv.client.dto.CvRequest;
import app.cv.client.dto.CvResponse;
import java.time.LocalDate;

public class DtoMapper {


//    public static CvRequest fromCv(Cv cv, UUID userId) {
//
//        return CvRequest.builder()
//                .id(cv.getId())
//
//                .fullName(cv.getFullName())
//                .birthDate(cv.getBirthDate())
//                .email(cv.getEmail())
//                .phone(cv.getPhone())
//                .location(cv.getLocation())
//                .linkedInUrl(cv.getLinkedInUrl())
//                .summary(cv.getSummary())
//
//                .jobTitle(cv.getJobTitle())
//                .company(cv.getCompany())
//                .experienceStartDate(formatDateForMonthInput(cv.getExperienceStartDate()))
//                .experienceEndDate(formatDateForMonthInput(cv.getExperienceEndDate()))
//                .experienceDescription(cv.getExperienceDescription())
//
//                .degree(cv.getDegree())
//                .school(cv.getSchool())
//                .educationStartDate(formatDateForMonthInput(cv.getEducationStartDate()))
//                .educationEndDate(formatDateForMonthInput(cv.getEducationEndDate()))
//                .educationInfo(cv.getEducationInfo())
//
//                .technicalSkills(cv.getTechnicalSkills())
//                .languages(cv.getLanguages())
//
//                .userId(userId)
//                .cvName(cv.getCvName())
//                .createdAt(cv.getCreatedAt() != null ? cv.getCreatedAt() : LocalDate.now())
//                .build();
//    }

    public static CvRequest fromCvResponse(CvResponse cv) {
        return CvRequest.builder()

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
                .experienceStartDate(formatDateForMonthInput(cv.getExperienceStartDate()))
                .experienceEndDate(formatDateForMonthInput(cv.getExperienceEndDate()))
                .experienceDescription(cv.getExperienceDescription())

                .degree(cv.getDegree())
                .school(cv.getSchool())
                .educationStartDate(formatDateForMonthInput(cv.getEducationStartDate()))
                .educationEndDate(formatDateForMonthInput(cv.getEducationEndDate()))
                .educationInfo(cv.getEducationInfo())

                .technicalSkills(cv.getTechnicalSkills())
                .languages(cv.getLanguages())

                .cvName(cv.getCvName())
                .createdAt(cv.getCreatedAt() != null ? cv.getCreatedAt() : LocalDate.now())
                .build();
    }

    private static String formatDateForMonthInput(LocalDate date) {
        if (date == null) {
            return null;
        }
        return String.format("%04d-%02d", date.getYear(), date.getMonthValue());
    }
}
