package roomit.main.domain.reservation.service;

import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.entity.ReservationState;
import roomit.main.domain.reservation.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;


    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void updateReservationStatus() {
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("스케줄링");

        List<Reservation> reservations = reservationRepository.findByReservationState(
                ReservationState.ACTIVE);

        for (Reservation reservation : reservations) {
            reservation.changeReservationState(ReservationState.COMPLETED);
            reservationRepository.save(reservation);
        }
    }
}
