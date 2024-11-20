package roomit.web1_2_bumblebee_be.domain.workplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;

import java.util.List;

public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {

    Workplace getWorkplaceByWorkplaceName(String workplaceName);
    Workplace getWorkplaceByWorkplacePhoneNumber(String workplacePhoneNumber);
    Workplace getWorkplaceByWorkplaceAddress(String workplaceAddress);

    // Business ID로 Workplace 목록 조회
    List<Workplace> findByBusiness_BusinessId(Long businessId);
}
