package roomit.main.domain.payments.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import roomit.main.domain.reservation.entity.Reservation;

import java.time.LocalDateTime;


@Entity
@Getter
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payments_id", nullable = false, unique = true)
    private Long paymentsId; // 결제 ID

    @Column(name = "order_id", nullable = false)
    private String orderId; //주문 ID (고유값)

    @Column(name = "order_name", nullable = false)
    private String orderName; //주문 상품 이름

    @Column(name = "toss_payments_key")
    private String tossPaymentsKey; //페이먼트키

    @Column(name = "total_amount", nullable = false)
    private Long totalAmount; //가격

    @Column(name = "member_name", nullable = false)
    private String memberName; //이름

    @Column(name = "member_phone_num", nullable = false)
    private String memberPhoneNum; //휴대폰

    @Enumerated(value = EnumType.STRING)
    @Column(name = "toss_payment_method", nullable = false)
    private TossPaymentMethod tossPaymentMethod; //결제 타입

    @Column(name = "pay_success_yn")
    private boolean paySuccessYN;

    @Column(name = "fail_reason")
    private String failReason;

    @Column(name = "cancel_yn")
    private boolean cancelYN;
    @Column(name = "cancel_reason")
    private String cancelReason;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "createdAt")
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Payments(String orderId,
                    String orderName,
                    String tossPaymentsKey,
                    Long totalAmount,
                    String memberName,
                    String memberPhoneNum,
                    TossPaymentMethod tossPaymentMethod,
                    boolean paySuccessYN,
                    String failReason,
                    boolean cancelYN,
                    String cancelReason,
                    Reservation reservation,
                    LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.tossPaymentsKey = tossPaymentsKey;
        this.totalAmount = totalAmount;
        this.memberName = memberName;
        this.memberPhoneNum = memberPhoneNum;
        this.tossPaymentMethod = tossPaymentMethod;
        this.paySuccessYN = paySuccessYN;
        this.failReason = failReason;
        this.cancelYN = cancelYN;
        this.cancelReason = cancelReason;
        this.reservation = reservation;
        this.createdAt = createdAt;
    }

    public void changeTossPaymentsKey(String tossPaymentsKey) {
        this.tossPaymentsKey = tossPaymentsKey;
    }

    public void changePaySuccessYN(boolean paySuccessYN) {
        this.paySuccessYN = paySuccessYN;
    }

    public void changeFailReason(String failReason) {
        this.failReason = failReason;
    }

    public void addReservation(Reservation reservation) {
        this.reservation = reservation;
        reservation.changePayments(this);
    }
}
