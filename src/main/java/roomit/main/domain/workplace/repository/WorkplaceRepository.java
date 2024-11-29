package roomit.main.domain.workplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.WorkplaceName;

import java.util.List;

public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {

    Workplace getWorkplaceByWorkplaceName(WorkplaceName workplaceName);

    @Query(value = """
        SELECT w.workplace_id, w.workplace_name, 
               w.workplace_address, w.image_url,
               w.star_sum, w.review_count,
               w.workplace_latitude, w.workplace_longitude,
               (6371 * acos(
                   cos(radians(:latitude)) * cos(radians(w.workplace_latitude)) *
                   cos(radians(w.workplace_longitude) - radians(:longitude)) +
                   sin(radians(:latitude)) * sin(radians(w.workplace_latitude))
               )) AS distance
        FROM workplace w 
        WHERE w.workplace_latitude BETWEEN :bottom AND :top 
          AND w.workplace_longitude BETWEEN :left AND :right 
        ORDER BY distance ASC
    """,  nativeQuery = true)
    List<Object[]> findAllByLatitudeAndLongitudeWithDistance(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("bottom") double bottom,
            @Param("top") double top,
            @Param("left") double left,
            @Param("right") double right);

    // Business ID로 Workplace 목록 조회
    List<Workplace> findByBusiness_BusinessId(Long businessId);

    @Modifying
    @Query("UPDATE Workplace w SET w.starSum = w.starSum + :rating, w.reviewCount = w.reviewCount + 1 WHERE w.workplaceId = :workplaceId")
    void updateRatingAndCount(@Param("rating") Long rating, @Param("workplaceId") Long workplaceId);
}
