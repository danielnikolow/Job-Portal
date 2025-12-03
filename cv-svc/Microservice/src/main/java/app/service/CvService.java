package app.service;
import app.model.Cv;
import app.repository.CvRepository;
import app.web.dto.CvRequest;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.apache.tomcat.util.http.FastHttpDateFormat.parseDate;

@Slf4j
@Service
public class CvService {

    private final CvRepository cvRepository;
    private final TemplateEngine templateEngine;
    private static final DateTimeFormatter MONTH_YEAR_FORMAT = DateTimeFormatter.ofPattern("MM/yyyy");

    @Autowired
    public CvService(CvRepository cvRepository, TemplateEngine templateEngine) {
        this.cvRepository = cvRepository;
        this.templateEngine = templateEngine;
    }

    public List<Cv> getCvsByUserId(UUID userId) {
        return cvRepository.findAllByUserId(userId);
    }

    public byte[] generateCvPdf(CvRequest dto) {

        Context context = new Context();
        context.setVariable("fullName", dto.getFullName());
        context.setVariable("birthDate", dto.getBirthDate());
        context.setVariable("email", dto.getEmail());
        context.setVariable("phone", dto.getPhone());
        context.setVariable("location", dto.getLocation());
        context.setVariable("profileUrl", dto.getLinkedInUrl());
        context.setVariable("summary", dto.getSummary());
        context.setVariable("experienceJobTitle", dto.getJobTitle());
        context.setVariable("experienceCompany", dto.getCompany());
        context.setVariable("experienceFrom", dto.getExperienceStartDate());
        context.setVariable("experienceTo", dto.getExperienceEndDate());
        context.setVariable("experienceDescription", dto.getExperienceDescription());
        context.setVariable("educationDegree", dto.getDegree());
        context.setVariable("educationSchool", dto.getSchool());
        context.setVariable("educationFrom", dto.getEducationStartDate());
        context.setVariable("educationTo", dto.getEducationEndDate());
        context.setVariable("educationInfo", dto.getEducationInfo());
        context.setVariable("technicalSkills", dto.getTechnicalSkills());
        context.setVariable("languages", dto.getLanguages());


        String html = templateEngine.process("cv-pdf-template", context);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            InputStream fontStream =
                    getClass().getResourceAsStream("/fonts/DejaVuLGCSans.ttf");

            if (fontStream == null) {
                throw new IllegalStateException("Не е намерен шрифт DejaVuLGCSans.ttf в /fonts");
            }
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFont(() -> fontStream, "DejaVu LGC Sans");
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Неуспешно генериране на PDF за CV", e);
        }
    }

    public Cv saveSv(CvRequest cvRequest) {

        Cv cv = Cv.builder()

                .fullName(cvRequest.getFullName())
                .birthDate(cvRequest.getBirthDate())
                .email(cvRequest.getEmail())
                .phone(cvRequest.getPhone())
                .location(cvRequest.getLocation())
                .linkedInUrl(cvRequest.getLinkedInUrl())
                .summary(cvRequest.getSummary())

                .jobTitle(cvRequest.getJobTitle())
                .company(cvRequest.getCompany())
                .experienceStartDate(parseDateField(String.valueOf(cvRequest.getExperienceStartDate())))
                .experienceEndDate(parseDateField(String.valueOf(cvRequest.getExperienceEndDate())))
                .experienceDescription(cvRequest.getExperienceDescription())

                .degree(cvRequest.getDegree())
                .school(cvRequest.getSchool())
                .educationStartDate(parseDateField(String.valueOf(cvRequest.getEducationStartDate())))
                .educationEndDate(parseDateField(String.valueOf(cvRequest.getEducationEndDate())))
                .educationInfo(cvRequest.getEducationInfo())

                .technicalSkills(cvRequest.getTechnicalSkills())
                .languages(cvRequest.getLanguages())

                .userId(cvRequest.getUserId())
                .cvName(cvRequest.getCvName())
                .createdAt(LocalDate.now())
                .build();

        return cvRepository.save(cv);
    }

    private LocalDate parseDateField(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            String trimmed = dateString.trim();
            // Handle month format "YYYY-MM" from HTML month input
            if (trimmed.matches("\\d{4}-\\d{2}")) {
                String[] parts = trimmed.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                // Validate month range
                if (month < 1 || month > 12) {
                    return null;
                }
                return LocalDate.of(year, month, 1);
            }
            // Try to parse as full date "YYYY-MM-DD"
            if (trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return LocalDate.parse(trimmed);
            }
            // Fallback to original parseDate if format is different
            return LocalDate.ofEpochDay(parseDate(trimmed));
        } catch (Exception e) {
            return null;
        }
    }

    public Void cvUpdate(CvRequest cvRequest) {

        Optional<Cv> cv = cvRepository.findById(cvRequest.getId());
        Cv curCv = cv.get();

        curCv.setFullName(cvRequest.getFullName());
        curCv.setBirthDate(cvRequest.getBirthDate());
        curCv.setEmail(cvRequest.getEmail());
        curCv.setPhone(cvRequest.getPhone());
        curCv.setLocation(cvRequest.getLocation());
        curCv.setLinkedInUrl(cvRequest.getLinkedInUrl());
        curCv.setSummary(cvRequest.getSummary());

        curCv.setJobTitle(cvRequest.getJobTitle());
        curCv.setCompany(cvRequest.getCompany());
        curCv.setExperienceStartDate(parseDateField(String.valueOf(cvRequest.getExperienceStartDate())));
        curCv.setExperienceEndDate(parseDateField(String.valueOf(cvRequest.getExperienceEndDate())));
        curCv.setExperienceDescription(cvRequest.getExperienceDescription());

        curCv.setDegree(cvRequest.getDegree());
        curCv.setSchool(cvRequest.getSchool());
        curCv.setEducationStartDate(parseDateField(String.valueOf(cvRequest.getEducationStartDate())));
        curCv.setEducationEndDate(parseDateField(String.valueOf(cvRequest.getEducationEndDate())));
        curCv.setEducationInfo(cvRequest.getEducationInfo());
        curCv.setTechnicalSkills(cvRequest.getTechnicalSkills());

        curCv.setLanguages(cvRequest.getLanguages());
        curCv.setCvName(cvRequest.getCvName());
        curCv.setUserId(cvRequest.getUserId());
        curCv.setCreatedAt(LocalDate.now());

        cvRepository.save(curCv);
        return null;
    }
}


