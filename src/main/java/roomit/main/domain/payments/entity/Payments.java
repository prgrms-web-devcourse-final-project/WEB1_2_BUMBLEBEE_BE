package roomit.main.domain.payments.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @Column(name = "payments_id")
    private Long paymentsId; //결제 id

    @Column(name = "order_id")
    private String orderId; //

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "tossPayments_kay")
    private String tossPaymentsKay;

    @Column(name = "total_amount")
    private int totalAmount;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "tosspaymentMethod", nullable = false)
    private TossPaymentMethod tosspaymentMethod;

    @Column(name = "tossPaymentStatus", nullable = false)
    private TossPaymentStatus tossPaymentStatus;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;

    @OneToOne
    @JoinColumn(name = "reservation_id" , nullable = false)
    private Reservation reservation;

    @Builder
    public Payments(String orderId,
                    String orderName,
                    String tossPaymentsKay,
                    int totalAmount,
                    TossPaymentMethod tosspaymentMethod,
                    TossPaymentStatus tossPaymentStatus,
                    LocalDateTime requestedAt,
                    LocalDateTime approvedAt,
                    Reservation reservation) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.tossPaymentsKay = tossPaymentsKay;
        this.totalAmount = totalAmount;
        this.tosspaymentMethod = tosspaymentMethod;
        this.tossPaymentStatus = tossPaymentStatus;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.reservation = reservation;
    }
}
