package roomit.main.domain.payments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.payments.Service.PaymentsService;
import roomit.main.domain.payments.dto.request.PaymentsRequest;
import roomit.main.domain.payments.dto.request.SaveAmountRequest;
import roomit.main.domain.payments.dto.response.PaymentsResponse;
import roomit.main.domain.payments.dto.response.PaymentsFailResponse;
import roomit.main.domain.payments.dto.response.PaymentsSuccessResponse;


import java.io.IOException;

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
    public PaymentsResponse requestTossPayment(@AuthenticationPrincipal CustomMemberDetails principal,
                                             @RequestParam(name = "reservationId", required = true) Long reservationId,
                                             @RequestBody @Valid PaymentsRequest request) {

        return paymentsService.requestPayment(reservationId,principal.getId(),request);
    }

    /**
     * 결제 성공 했을때
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/toss/success")
    public PaymentsSuccessResponse tossPaymentSuccess(
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
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId
    ) {
        return paymentsService.tossPaymentFail(code, message, orderId);
    }

}
