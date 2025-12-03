package app.cv.service;
import app.cv.client.CvClient;
import app.cv.client.dto.CvRequest;
import app.cv.client.dto.CvResponse;
import app.cv.client.mapper.DtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CvService {

    private final CvClient cvExportClient;
    private final CvClient cvClient;

    public CvService(CvClient cvExportClient, CvClient cvClient) {
        this.cvExportClient = cvExportClient;
        this.cvClient = cvClient;
    }

    public List<CvRequest> getCvsByUserId(UUID userId) {

        List<CvResponse> cvsResponse = cvClient.getCvsByUserId(userId).getBody();
        List<CvRequest> cvs = cvsResponse.stream().map(DtoMapper::fromCvResponse).toList();

        return cvs;
    }

    public void updateCv(CvRequest cvRequest, UUID uuid) {
        cvRequest.setUserId(uuid);
        cvClient.updateCv(cvRequest);
    }

    public void saveCv(CvRequest cvRequest, UUID uuid) {

        cvRequest.setUserId(uuid);
        cvClient.saveCv(cvRequest);
    }

    public byte[] exportUserCv(CvRequest cvRequest, UUID id) {

        cvRequest.setUserId(id);
        ResponseEntity<byte[]> response = cvExportClient.exportCv(cvRequest);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException("Неуспешен отговор от CV export микросервиса");
        }

        return response.getBody();
    }

}
