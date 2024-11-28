package roomit.main.domain.payments.dto.response;

import jakarta.validation.constraints.NotNull;
import roomit.main.domain.payments.entity.TossPaymentMethod;

import java.time.LocalDateTime;

public record PaymentsResponse(
        @NotNull TossPaymentMethod tosspaymentMethod,
        @NotNull int totalAmount,
        @NotNull String orderName,
        @NotNull String orderId,
        @NotNull String memberName,
        @NotNull String memberPhoneNum,
        @NotNull String successUrl,
        @NotNull String failUrl,
        @NotNull LocalDateTime approvedAt,
        @NotNull String paySuccessYn
) {}
