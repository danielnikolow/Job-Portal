package app.repository;

import java.util.List;
import java.util.UUID;
import app.model.EmploymentType;
import app.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, UUID> {

    @Query("""
            SELECT j
            FROM Job j
            WHERE j.active = true
              AND (
                    :keyword IS NULL
                    OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(j.summary) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
              AND (
                    :location IS NULL
                    OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))
                  )
              AND (
                    :employmentType IS NULL
                    OR j.employmentType = :employmentType
                  )
            ORDER BY j.postedOn DESC
            """)
    List<Job> searchOffers(@Param("keyword") String keyword,
                           @Param("location") String location,
                           @Param("employmentType") EmploymentType employmentType);
}


