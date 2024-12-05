package roomit.main.domain.payments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.payments.Service.PaymentsService;
import roomit.main.domain.payments.dto.request.PaymentsRequest;
import roomit.main.domain.payments.dto.response.PaymentValidationResponse;
import roomit.main.domain.payments.dto.response.PaymentsFailResponse;
import roomit.main.domain.payments.dto.response.PaymentsResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentsRestController {
    private final PaymentsService paymentsService;
    private final ObjectMapper objectMapper;

    /**
     * 검증 및 서버 저장
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/toss")
    public PaymentValidationResponse requestTossPayment(@AuthenticationPrincipal CustomMemberDetails principal,
                                                        @RequestParam(name = "reservationId", required = true) Long reservationId,
                                                        @RequestBody @Valid PaymentsRequest request) {

        return paymentsService.requestPayment(reservationId,principal.getId(),request);
    }

    /**
     * 결제 성공 했을때
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/toss/success")
    public PaymentsResponse tossPaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) {

        return paymentsService.tossPaymentSuccess(paymentKey, orderId, amount);
    }

    /**
     * 결제 실패 했을때
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/toss/fail")
    public PaymentsFailResponse tossPaymentFail(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "message") String message,
            @RequestParam(name = "orderId") String orderId
    ) {
        return paymentsService.tossPaymentFail(code, message, orderId);
    }

    /**
     * 결제 취소 했을때
     */
    @PostMapping("/toss/cancel")
    public void tossPaymentCancelPoint(
            @RequestParam String paymentKey,
            @RequestParam String cancelReason
    ) {
        paymentsService.cancelPayments(paymentKey, cancelReason);
    }
}
