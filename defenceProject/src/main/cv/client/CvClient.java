package app.cv.client;

import app.cv.client.dto.CvRequest;
import app.cv.client.dto.CvResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@FeignClient(name = "cv-export-service", url = "http://localhost:8081/api/v1")
public interface CvClient {

    @GetMapping("/get-cvs")
    ResponseEntity<List<CvResponse>> getCvsByUserId(@RequestParam UUID userId);

    @PutMapping("/cv-update")
    ResponseEntity<Void> updateCv(@RequestBody CvRequest cvRequest);

    @PostMapping("/cv-save")
    ResponseEntity<Void> saveCv(@RequestBody CvRequest cvRequest);

    @PostMapping("/cv-export")
    ResponseEntity<byte[]> exportCv(@RequestBody CvRequest cvRequest);

}