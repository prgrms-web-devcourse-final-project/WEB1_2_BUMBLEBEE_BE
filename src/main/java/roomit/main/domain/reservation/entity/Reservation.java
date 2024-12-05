package roomit.main.domain.reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.payments.entity.Payments;
import roomit.main.domain.reservation.dto.request.UpdateReservationRequest;
import roomit.main.domain.reservation.entity.value.ReservationName;
import roomit.main.domain.reservation.entity.value.ReservationNum;
import roomit.main.domain.review.entity.Review;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.entity.value.BaseEntity;

@Table(name = "Reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Reservation extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", unique = true, updatable = false, columnDefinition = "BIGINT")
    private Long reservationId;

    @Embedded
    private ReservationName reservationName;

    @Embedded
    private ReservationNum reservationPhoneNumber;

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
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyroom_id")
    private StudyRoom studyRoom;

    @OneToOne
    @JoinColumn(name = "review_id") // 외래 키 이름 설정
    private Review review;

    @OneToOne(mappedBy = "reservation")
    private Payments payments;


    @Builder
    public Reservation(String reservationName, String reservationPhoneNumber, ReservationState reservationState, Integer reservationCapacity,Integer reservationPrice,LocalDateTime startTime, LocalDateTime endTime, Member member, StudyRoom studyRoom) {
        this.reservationName = new ReservationName(reservationName);
        this.reservationPhoneNumber = new ReservationNum(reservationPhoneNumber);
        this.reservationState = reservationState;
        this.reservationCapacity = reservationCapacity;
        this.reservationPrice = reservationPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.member = member;
        this.studyRoom = studyRoom;
    }

    public void changereservationName(String reservationName) {
        this.reservationName = new ReservationName(reservationName);
    }

    public void changeReservationState(ReservationState reservationState) {
        this.reservationState = reservationState;
    }

    public void changeReservationCapacity(Integer reservationCapacity) {
        this.reservationCapacity = reservationCapacity;
    }

    public void changeReservationPrice(Integer reservationPrice) {
        this.reservationPrice = reservationPrice;
    }

    public void changeStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void changeEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void changeMember(Member member) {
        this.member = member;
    }

    public void changeStudyRoom(StudyRoom studyRoom) {
        this.studyRoom = studyRoom;
    }

    public void changePayments(Payments payments) {
        this.payments = payments;
    }

    public void updateReservationDetails(String reservationName, String reservationPhoneNumber,
                                         LocalDateTime startTime, LocalDateTime endTime) {
        this.reservationName = new ReservationName(reservationName);
        this.reservationPhoneNumber = new ReservationNum(reservationPhoneNumber);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void addReview(Review review) {
        this.review = review;
        review.changeReservation(this); // Review에도 역방향 관계 설정
    }

    @PreRemove
    private void preRemove() {
        if (review != null) {
            review.changeReservation(null); // 리뷰와의 연결을 끊음
        }
    }
}
