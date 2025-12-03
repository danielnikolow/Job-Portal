package app.repository;

import app.model.Cv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CvRepository extends JpaRepository<Cv, UUID> {

    List<Cv> findAllByUserId(UUID userId);
}