package roomit.main.domain.payments.dto.response;

import lombok.Builder;

@Builder
public record PaymentsFailResponse(
        String errorCode,
        String errorMessage,
        String orderId
) {}
