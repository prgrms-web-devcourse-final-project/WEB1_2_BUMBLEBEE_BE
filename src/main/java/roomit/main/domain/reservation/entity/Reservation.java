package roomit.main.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.studyroom.entity.BaseEntity;
import roomit.main.domain.studyroom.entity.StudyRoom;

import java.time.LocalDateTime;

@Table(name = "Reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class Reservation extends BaseEntity{

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

    @Column(name = "reservation_capacity", nullable = false, columnDefinition = "INTEGER")
    private Integer reservationCapacity;

    @Column(name= "reservation_price", nullable = false, columnDefinition = "INTEGER")
    private Integer reservationPrice;

    @Column(name = "start_time", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyroom_id")
    private StudyRoom studyRoomId;

    @Builder
    public Reservation(String reservationName, String reservationPhoneNumber, ReservationState reservationState, Integer reservationCapacity,Integer reservationPrice,LocalDateTime startTime, LocalDateTime endTime, Member memberId, StudyRoom studyRoomId) {
        this.reservationName = reservationName;
        this.reservationPhoneNumber = reservationPhoneNumber;
        this.reservationState = reservationState;
        this.reservationCapacity = reservationCapacity;
        this.reservationPrice = reservationPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.memberId = memberId;
        this.studyRoomId = studyRoomId;
    }

    @PrePersist
    public void prePersist() {
        if (this.reservationState == null) {
            this.reservationState = ReservationState.RESERVABLE;
        }
    }
}
