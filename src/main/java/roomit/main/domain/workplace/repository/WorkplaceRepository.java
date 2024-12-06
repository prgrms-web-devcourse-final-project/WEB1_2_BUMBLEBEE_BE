package roomit.main.domain.workplace.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.WorkplaceName;

public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {

    Workplace getWorkplaceByWorkplaceName(WorkplaceName workplaceName);

    @Query(value = """
                SELECT w.workplace_id, w.workplace_name, 
                       w.workplace_address, w.image_url,
                       w.star_sum, w.review_count,
                       ST_X(w.location) AS longitude,
                        ST_Y(w.location) AS latitude,
                        ST_Distance(
                            ST_GeomFromText(:referencePoint, 5181),
                            w.location
                        ) AS distance
                 FROM workplace w
                 WHERE ST_Within(
                           w.location,
                           ST_GeomFromText(:area, 5181)
                       )
                 ORDER BY distance ASC
            """, nativeQuery = true)
    List<Object[]> findAllWithinArea(
            @Param("referencePoint") String referencePoint,
            @Param("area") String area
    );

    // Business ID로 Workplace 목록 조회
    @Query("SELECT w FROM Workplace w WHERE w.business.businessId = :businessId")
    List<Workplace> findByBusinessId(Long businessId);

    @Modifying
    @Query("UPDATE Workplace w SET w.starSum = w.starSum + :rating, w.reviewCount = w.reviewCount + 1 WHERE w.workplaceId = :workplaceId")
    void updateRatingAndCount(@Param("rating") Long rating, @Param("workplaceId") Long workplaceId);

    @Query(value = """
        SELECT w.workplace_id,
               ST_Distance_Sphere(
                   point(w.workplace_longitude, w.workplace_latitude),
                   point(:longitude, :latitude)
               ) AS distance
        FROM workplace w
        WHERE ST_Distance_Sphere(
            point(w.workplace_longitude, w.workplace_latitude),
            point(:longitude, :latitude)
        ) <= :maxDistance
    """, nativeQuery = true)
    List<Object[]> findNearbyWorkplaces(double longitude, double latitude, double maxDistance);
}
