package roomit.main.domain.payments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomit.main.domain.payments.entity.Payments;
import roomit.main.domain.reservation.entity.Reservation;

import java.util.Optional;


public interface PaymentsRepository extends JpaRepository<Payments,Long> {

    Optional<Payments> findByOrderId(String orderId);

    Optional<Payments> findByTossPaymentsKey(String paymentKey);

    @Query("SELECT p.reservation FROM Payments p WHERE p = :payments")
    Optional<Reservation> findReservationByPayments(Payments payments);


}
