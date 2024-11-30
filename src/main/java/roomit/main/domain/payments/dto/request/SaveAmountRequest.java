package roomit.main.domain.payments.dto.request;

import jakarta.validation.constraints.NotNull;

public record SaveAmountRequest(
        @NotNull int totalAmount,
        @NotNull String orderId
) {}
