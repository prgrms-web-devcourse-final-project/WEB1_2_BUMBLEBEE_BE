package roomit.main.domain.payments.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import roomit.main.domain.payments.dto.response.PaymentsResponse;
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
    private String orderId;

    @Column(name = "order_name", nullable = false)
    private String orderName;

    @Column(name = "toss_payments_key", nullable = false)
    private String tossPaymentsKey;

    @Column(name = "total_amount", nullable = false)
    private int totalAmount;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_phone_num", nullable = false)
    private String memberPhoneNum;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "toss_payment_method", nullable = false)
    private TossPaymentMethod tossPaymentMethod;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "toss_payment_status", nullable = false)
    private TossPaymentStatus tossPaymentStatus;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt; //결제 시작

    @Column(name = "approved_at")
    private LocalDateTime approvedAt; // 결제 승인

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Builder
    public Payments(String orderId,
                    String orderName,
                    String tossPaymentsKey,
                    int totalAmount,
                    String memberName,
                    String memberPhoneNum,
                    TossPaymentMethod tossPaymentMethod,
                    TossPaymentStatus tossPaymentStatus,
                    Reservation reservation,
                    LocalDateTime approvedAt) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.tossPaymentsKey = tossPaymentsKey;
        this.totalAmount = totalAmount;
        this.memberName = memberName;
        this.memberPhoneNum = memberPhoneNum;
        this.tossPaymentMethod = tossPaymentMethod;
        this.tossPaymentStatus = tossPaymentStatus;
        this.reservation = reservation;
        this.approvedAt = approvedAt;
        this.requestedAt = LocalDateTime.now();
    }
    public PaymentsResponse toDto(){
        return new PaymentsResponse (
                tossPaymentMethod,
                totalAmount,
                orderName,
                orderId,
                memberName,
                memberPhoneNum,
                "",
                "",
                approvedAt,
                "y");
    }
}
