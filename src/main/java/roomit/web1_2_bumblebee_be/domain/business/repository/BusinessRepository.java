package roomit.web1_2_bumblebee_be.domain.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomit.web1_2_bumblebee_be.domain.business.entity.Business;

public interface BusinessRepository extends JpaRepository<Business, Long> {
}
