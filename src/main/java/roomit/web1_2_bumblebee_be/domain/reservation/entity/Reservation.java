package roomit.web1_2_bumblebee_be.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;

import java.time.LocalDateTime;

@Table(name = "Reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", unique = true, updatable = false, columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "reservation_name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String reservationName;

    @Column(name = "reservation_phone_number", nullable = false, columnDefinition = "VARCHAR(20)")
    private String reservationPhoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_state", nullable = false, columnDefinition = "VARCHAR(20)")
    private ReservationState reservationState;

    @Column(name = "start_time", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyroom_id")
    private StudyRoom studyRoom;
}
