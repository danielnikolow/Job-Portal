package main.repository;

import java.util.List;
import java.util.UUID;
import main.model.EmploymentType;
import main.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OfferRepository extends JpaRepository<Offer, UUID> {

    @Query("""
            SELECT o
            FROM Offer o
            WHERE o.active = true
              AND (
                    :keyword IS NULL
                    OR LOWER(o.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(o.company) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(o.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
              AND (
                    :location IS NULL
                    OR LOWER(o.location) LIKE LOWER(CONCAT('%', :location, '%'))
                  )
              AND (
                    :employmentType IS NULL
                    OR o.employmentType = :employmentType
                  )
            ORDER BY o.postedOn DESC
            """)
    List<Offer> searchOffers(@Param("keyword") String keyword,
                             @Param("location") String location,
                             @Param("employmentType") EmploymentType employmentType);
}


