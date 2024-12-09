package roomit.main.domain.workplace.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.WorkplaceName;
import roomit.main.domain.workplace.repository.search.WorkplaceSearch;

public interface WorkplaceRepository extends JpaRepository<Workplace, Long> , WorkplaceSearch {

    Workplace getWorkplaceByWorkplaceName(WorkplaceName workplaceName);

    @Query(value = """
    SELECT w.workplace_id, w.workplace_name, 
           w.workplace_address, w.image_url,
           w.star_sum, w.review_count,
           ST_X(w.location) AS longitude,
           ST_Y(w.location) AS latitude,
           ST_Distance(
               ST_Transform(ST_GeomFromText(:referencePoint, 5181), ST_SRID(w.location)),
               w.location
           ) * 100 AS distance
    FROM workplace w
    WHERE ST_Within(
              w.location,
              ST_Transform(ST_GeomFromText(:area, 5181), ST_SRID(w.location))
          )
    ORDER BY distance ASC
    LIMIT 10
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

}
