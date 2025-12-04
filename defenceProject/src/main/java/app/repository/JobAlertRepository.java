package app.repository;

import app.model.JobAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobAlertRepository extends JpaRepository<JobAlert, UUID> {
    List<JobAlert> findByActiveTrue();
    List<JobAlert> findByUserId(UUID userId);
    List<JobAlert> findByUserIdAndActiveTrue(UUID userId);
}

