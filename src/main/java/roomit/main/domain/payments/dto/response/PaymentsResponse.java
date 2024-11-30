package roomit.main.domain.payments.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import roomit.main.domain.payments.entity.TossPaymentMethod;

import java.time.LocalDateTime;


@Builder
public record PaymentsResponse(
        @NotNull TossPaymentMethod tossPaymentMethod, // 결제 타입 - 카드/현금/포인트
        @NotNull Long amount, // 가격 정보
        @NotNull String orderName, // 주문명
        @NotNull String orderId,
        @NotNull String memberPhoneNum, // 고객 번호
        @NotNull String memberName, // 고객 이름
        String successUrl, // 성공 시 리다이렉트 될 URL
        String failUrl, // 실패 시 리다이렉트 될 URL
        String failReason, // 실패 이유
        boolean cancelYN, // 취소 YN
        String cancelReason, // 취소 이유
        LocalDateTime createdAt // 결제가 이루어진 시간
) {}
