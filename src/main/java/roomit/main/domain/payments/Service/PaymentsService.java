package roomit.main.domain.payments.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import roomit.main.domain.payments.config.PaymentsConfig;
import roomit.main.domain.payments.dto.request.PaymentsRequest;
import roomit.main.domain.payments.dto.response.PaymentsFailResponse;
import roomit.main.domain.payments.dto.response.PaymentsResponse;
import roomit.main.domain.payments.dto.response.PaymentsSuccessResponse;
import roomit.main.domain.payments.entity.Payments;
import roomit.main.domain.payments.repository.PaymentsRepository;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.entity.ReservationState;
import roomit.main.domain.reservation.repository.ReservationRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import net.minidev.json.JSONObject;
import roomit.main.global.error.ErrorCode;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentsService {

    private final PaymentsRepository paymentsRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentsConfig paymentsConfig;

    /**
     * 결제 검증
     */

    public PaymentsResponse requestPayment(Long reservationId, Long memberId, PaymentsRequest paymentsRequest) { // 결제 승인 요청

        validateReservationForPayment(reservationId,memberId,paymentsRequest); // 검증

        Payments payments = paymentsRequest.toEntity();
        paymentsRepository.save(payments); //서버에 저장 db저장

        return PaymentsResponse.builder()
                .orderId(payments.getOrderId())
                .orderName(payments.getOrderName())
                .memberName(payments.getMemberName())
                .memberPhoneNum(payments.getMemberPhoneNum())
                .tossPaymentMethod(payments.getTossPaymentMethod())
                .amount(payments.getTotalAmount())
                .createdAt(payments.getCreatedAt())
                .successUrl(paymentsConfig.getSuccessUrl())
                .failUrl(paymentsConfig.getFailUrl()).build();
    }

    /**
     * 결제 성공
     */
    @Transactional
    public PaymentsSuccessResponse tossPaymentSuccess(String paymentKey, String orderId, Long amount) {
        Payments payments = verifyPayment(orderId, amount);
        PaymentsSuccessResponse result = requestPaymentAccept(paymentKey, orderId, amount);
        payments.changeTossPaymentsKey(paymentKey);
        payments.changePaySuccessYN(true);
        return result;
    }

    /**
     * (토스)결제 승인 요청
     */
    @Transactional
    public PaymentsSuccessResponse requestPaymentAccept(String paymentKey, String orderId, Long amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();
        params.put("orderId", orderId);
        params.put("amount", amount);

        PaymentsSuccessResponse result = null;
        try {
            result = restTemplate.postForObject(PaymentsConfig.URL + paymentKey,
                    new HttpEntity<>(params, headers),
                    PaymentsSuccessResponse.class);
        } catch (Exception e) {
            throw ErrorCode.PAYMENTS_ALREADY_APPROVED.commonException();
        }

        return result;

    }

    /**
     * 결제 요청된 금액과 실제 결제된 금액이 같은지 검증
     */
    public Payments verifyPayment(String orderId, Long amount) {
        Payments payment = paymentsRepository.findByOrderId(orderId).orElseThrow(ErrorCode.PAYMENTS_NOT_FOUND::commonException);
        if (!payment.getTotalAmount().equals(amount)) {
            throw ErrorCode.PAYMENTS_AMOUNT_EXP.commonException();
        }
        return payment;
    }


    /**
     * 결제 실패
     */
    @Transactional
    public PaymentsFailResponse tossPaymentFail(String code, String message, String orderId) {
        Payments payment = paymentsRepository.findByOrderId(orderId).orElseThrow(ErrorCode.PAYMENTS_NOT_FOUND::commonException);

        payment.changePaySuccessYN(false);
        payment.changeFailReason(message);

        return PaymentsFailResponse.builder()
                .errorCode(code)
                .errorMessage(message)
                .orderId(orderId)
                .build();
    }

    /**
     * 예약 및 결제 검증 로직
     */
    public void validateReservationForPayment(Long reservationId, Long memberId, PaymentsRequest paymentRequest) {

        // 예약 조회 및 소유권 검증
        Reservation reservation = reservationRepository.findFirstByIdAndMemberId(reservationId, memberId)
                .orElseThrow(() -> null); // 예외 추가해야함

        // 예약 상태 검증
        if(reservation.getReservationState().equals(ReservationState.COMPLETED)){
            throw null; //예외 추가해야함
        }

        // 결제 금액 검증
        if (paymentRequest.totalAmount() < 1000) {
            throw ErrorCode.PAYMENTS_INVALID_AMOUNT.commonException();
        }
    }


    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = new String(
                Base64.getEncoder().encode((paymentsConfig.getTestSecretKey() + ":").getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
