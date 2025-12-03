package app.cv.client.mapper;
import app.cv.client.dto.CvRequest;
import app.cv.client.dto.CvResponse;
import app.model.Cv;
import java.time.LocalDate;
import java.util.UUID;

public class DtoMapper {


    public static CvRequest fromCv(Cv cv, UUID userId) {

        return CvRequest.builder()
                // ID
                .id(cv.getId())
                
                // Step 1: Основни данни
                .fullName(cv.getFullName())
                .birthDate(cv.getBirthDate())
                .email(cv.getEmail())
                .phone(cv.getPhone())
                .location(cv.getLocation())
                .linkedInUrl(cv.getLinkedInUrl())
                .summary(cv.getSummary())

                // Step 2: Опит
                .jobTitle(cv.getJobTitle())
                .company(cv.getCompany())
                .experienceStartDate(formatDateForMonthInput(cv.getExperienceStartDate()))
                .experienceEndDate(formatDateForMonthInput(cv.getExperienceEndDate()))
                .experienceDescription(cv.getExperienceDescription())

                // Step 3: Образование
                .degree(cv.getDegree())
                .school(cv.getSchool())
                .educationStartDate(formatDateForMonthInput(cv.getEducationStartDate()))
                .educationEndDate(formatDateForMonthInput(cv.getEducationEndDate()))
                .educationInfo(cv.getEducationInfo())

                // Step 4: Умения
                .technicalSkills(cv.getTechnicalSkills())
                .languages(cv.getLanguages())

                // Step 5: Финални настройки
                .userId(userId)
                .cvName(cv.getCvName())
                .createdAt(cv.getCreatedAt() != null ? cv.getCreatedAt() : LocalDate.now())
                .build();
    }

    public static CvRequest fromCvResponse(CvResponse cv) {
        return CvRequest.builder()
                // ID
                .id(cv.getId())

                // Step 1: Основни данни
                .fullName(cv.getFullName())
                .birthDate(cv.getBirthDate())
                .email(cv.getEmail())
                .phone(cv.getPhone())
                .location(cv.getLocation())
                .linkedInUrl(cv.getLinkedInUrl())
                .summary(cv.getSummary())

                // Step 2: Опит
                .jobTitle(cv.getJobTitle())
                .company(cv.getCompany())
                .experienceStartDate(formatDateForMonthInput(cv.getExperienceStartDate()))
                .experienceEndDate(formatDateForMonthInput(cv.getExperienceEndDate()))
                .experienceDescription(cv.getExperienceDescription())

                // Step 3: Образование
                .degree(cv.getDegree())
                .school(cv.getSchool())
                .educationStartDate(formatDateForMonthInput(cv.getEducationStartDate()))
                .educationEndDate(formatDateForMonthInput(cv.getEducationEndDate()))
                .educationInfo(cv.getEducationInfo())

                // Step 4: Умения
                .technicalSkills(cv.getTechnicalSkills())
                .languages(cv.getLanguages())

                // Step 5: Финални настройки
                .cvName(cv.getCvName())
                .createdAt(cv.getCreatedAt() != null ? cv.getCreatedAt() : LocalDate.now())
                .build();
    }

    private static String formatDateForMonthInput(LocalDate date) {
        if (date == null) {
            return null;
        }
        // Format as YYYY-MM for month input type
        return String.format("%04d-%02d", date.getYear(), date.getMonthValue());
    }

//    private static String parseDateField(LocalDate date) {
//        if (date == null) {
//            return null;
//        }
//        return date.toString(); // формат YYYY-MM-DD
//    }

//    private static String parseDateField(String dateString) {
//        if (dateString == null || dateString.trim().isEmpty()) {
//            return null;
//        }
//        try {
//            String trimmed = dateString.trim();
//            // Handle month format "YYYY-MM" from HTML month input
//            if (trimmed.matches("\\d{4}-\\d{2}")) {
//                String[] parts = trimmed.split("-");
//                int year = Integer.parseInt(parts[0]);
//                int month = Integer.parseInt(parts[1]);
//                // Validate month range
//                if (month < 1 || month > 12) {
//                    return null;
//                }
//                return LocalDate.of(year, month, 1);
//            }
//            // Try to parse as full date "YYYY-MM-DD"
//            if (trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
//                return LocalDate.parse(trimmed);
//            }
//            // Fallback to original parseDate if format is different
//            return LocalDate.ofEpochDay(parseDate(trimmed));
//        } catch (Exception e) {
//            return null;
//        }
//    }
}
