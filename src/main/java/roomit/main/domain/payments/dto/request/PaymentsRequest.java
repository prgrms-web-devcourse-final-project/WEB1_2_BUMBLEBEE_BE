package roomit.main.domain.payments.dto.request;

import jakarta.validation.constraints.NotNull;
import roomit.main.domain.payments.entity.Payments;
import roomit.main.domain.payments.entity.TossPaymentMethod;

import java.util.UUID;

public record PaymentsRequest(
        @NotNull String orderId,
        @NotNull String orderName,
        @NotNull Long totalAmount,
        @NotNull String memberName,
        @NotNull String memberPhoneNum,
        @NotNull TossPaymentMethod tossPaymentMethod
) {

    public Payments toEntity() {
        return Payments.builder()
                .orderId(orderId)
                .tossPaymentMethod(tossPaymentMethod)
                .totalAmount(totalAmount)
                .orderName(orderName)
                .memberName(memberName)
                .memberPhoneNum(memberPhoneNum)
                .build();
    }
}
