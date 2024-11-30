package roomit.main.domain.payments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomit.main.domain.payments.entity.Payments;

import java.util.Optional;


public interface PaymentsRepository extends JpaRepository<Payments,Long> {

    Optional<Payments> findByOrderId(String orderId);


}
