package roomit.web1_2_bumblebee_be.domain.workplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplaceAddress;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplaceName;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplacePhoneNumber;

import java.util.List;

public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {

    Workplace getWorkplaceByWorkplaceName(WorkplaceName workplaceName);
    Workplace getWorkplaceByWorkplacePhoneNumber(WorkplacePhoneNumber workplacePhoneNumber);
    Workplace getWorkplaceByWorkplaceAddress(WorkplaceAddress workplaceAddress);

    // Business ID로 Workplace 목록 조회
    List<Workplace> findByBusiness_BusinessId(Long businessId);
}
