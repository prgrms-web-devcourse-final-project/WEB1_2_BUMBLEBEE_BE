package roomit.main.domain.payments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomit.main.domain.payments.entity.Payments;
import roomit.main.domain.reservation.entity.Reservation;


public interface PaymentsRepository extends JpaRepository<Payments,Long> {


}
