package roomit.main.domain.payments.dto.request;

import jakarta.validation.constraints.NotNull;
import roomit.main.domain.payments.entity.Payments;
import roomit.main.domain.payments.entity.TossPaymentMethod;

import java.util.UUID;

public record PaymentsRequest(
        @NotNull String tossPaymentsKey,
        @NotNull String orderId,
        @NotNull TossPaymentMethod tossPaymentMethod,
        @NotNull int totalAmount,
        @NotNull String orderName, // 스터디룸A
        @NotNull String memberName,
        @NotNull String memberPhoneNum
) {

    public Payments toEntity() {
        return Payments.builder()
                .tossPaymentMethod(tossPaymentMethod)
                .totalAmount(totalAmount)
                .orderName(orderName)
                .orderId(UUID.randomUUID().toString())
                .build();
    }
}
