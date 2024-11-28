package roomit.main.domain.workplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.WorkplaceAddress;
import roomit.main.domain.workplace.entity.value.WorkplaceName;
import roomit.main.domain.workplace.entity.value.WorkplacePhoneNumber;

import java.util.List;

public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {

    Workplace getWorkplaceByWorkplaceName(WorkplaceName workplaceName);
    Workplace getWorkplaceByWorkplacePhoneNumber(WorkplacePhoneNumber workplacePhoneNumber);
    Workplace getWorkplaceByWorkplaceAddress(WorkplaceAddress workplaceAddress);

    @Query(value = "SELECT w.workplace_id, w.workplace_name, w.workplace_phone_number, " +
            "w.workplace_description, w.workplace_address, w.image_url, " +
            "w.workplace_start_time, w.workplace_end_time, w.created_at, " +
            "w.workplace_latitude, w.workplace_longitude, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(w.workplace_latitude)) " +
            "* cos(radians(w.workplace_longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(w.workplace_latitude)))) AS distance " +
            "FROM workplace w " +
            "WHERE w.workplace_latitude BETWEEN :bottom AND :top " +
            "AND w.workplace_longitude BETWEEN :left AND :right " +
            "ORDER BY distance ASC", nativeQuery = true)
    List<Object[]> findAllByLatitudeAndLongitudeWithDistance(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("bottom") double bottom,
            @Param("top") double top,
            @Param("left") double left,
            @Param("right") double right);




    // Business ID로 Workplace 목록 조회
    List<Workplace> findByBusiness_BusinessId(Long businessId);
}
