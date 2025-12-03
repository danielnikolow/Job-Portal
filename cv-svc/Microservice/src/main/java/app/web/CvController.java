package app.web;
import app.service.CvService;
import app.web.dto.CvRequest;
import app.web.dto.CvResponse;
import app.web.mapper.DtoMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CvController {

    private final CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    @GetMapping("/get-cvs")
    public ResponseEntity<List<CvResponse>> getCvsByUserId(@RequestParam(name = "userId") UUID userId) {

        List<CvResponse> cvs = cvService.getCvsByUserId(userId).stream().map(DtoMapper::fromCv).toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cvs);
    }

    @PostMapping("/cv-save")
    public ResponseEntity<Void> saveCv(@RequestBody CvRequest cvRequest) {

        cvService.saveSv(cvRequest);

        return ResponseEntity
                .ok()
                .body(null);
    }

    @PutMapping("/cv-update")
    public ResponseEntity<Void> cvUpdate(@RequestBody CvRequest cvRequest) {

        cvService.cvUpdate(cvRequest);

        return ResponseEntity
                .ok()
                .body(null);
    }

    @PostMapping("/cv-export")
    public ResponseEntity<byte[]> exportCv(@RequestBody CvRequest cvRequest) {

        byte[] pdfBytes = cvService.generateCvPdf(cvRequest);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cv.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}