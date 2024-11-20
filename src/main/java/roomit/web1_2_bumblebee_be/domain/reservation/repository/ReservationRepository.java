package roomit.web1_2_bumblebee_be.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomit.web1_2_bumblebee_be.domain.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

}
