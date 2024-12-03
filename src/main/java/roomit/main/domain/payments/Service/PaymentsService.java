package roomit.main.domain.payments.Service;

import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import roomit.main.domain.payments.config.PaymentsConfig;
import roomit.main.domain.payments.dto.request.PaymentsRequest;
import roomit.main.domain.payments.dto.response.PaymentsFailResponse;
import roomit.main.domain.payments.dto.response.PaymentValidationResponse;
import roomit.main.domain.payments.dto.response.PaymentsResponse;
import roomit.main.domain.payments.entity.Payments;
import roomit.main.domain.payments.repository.PaymentsRepository;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.entity.ReservationState;
import roomit.main.domain.reservation.repository.ReservationRepository;
import roomit.main.global.error.ErrorCode;

@Service
@RequiredArgsConstructor
public class PaymentsService {

    private final PaymentsRepository paymentsRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentsConfig paymentsConfig;

    /**
     * 결제 검증
     */

    public PaymentValidationResponse requestPayment(Long reservationId, Long memberId, PaymentsRequest paymentsRequest) { // 결제 승인 요청

        validateReservationForPayment(reservationId,memberId,paymentsRequest); // 검증

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ErrorCode.RESERVATION_NOT_FOUND::commonException);

        Payments payments = paymentsRequest.toEntity();
        payments.addReservation(reservation);
        paymentsRepository.save(payments); //서버에 저장 db저장

        reservation.changeReservationState(ReservationState.COMPLETED);

        return PaymentValidationResponse.builder()
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
    public PaymentsResponse tossPaymentSuccess(String paymentKey, String orderId, Long amount) {
        Payments payments = verifyPayment(orderId, amount);
        PaymentsResponse result = requestPaymentAccept(paymentKey, orderId, amount);
        payments.changeTossPaymentsKey(paymentKey);
        payments.changePaySuccessYN(true);
        return result;
    }

    /**
     * (토스)결제 승인 요청
     */
    @Transactional
    public PaymentsResponse requestPaymentAccept(String paymentKey, String orderId, Long amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();
        params.put("paymentKey", paymentKey);
        params.put("orderId", orderId);
        params.put("amount", amount);

        try {
            return restTemplate.postForObject(PaymentsConfig.URL+"/confirm",
                    new HttpEntity<>(params, headers),
                    PaymentsResponse.class);
        } catch (Exception e) {
            throw e; //예외추가해야함
        }


    }

    @Transactional
    public Map cancelPayments(String paymentKey, String cancelReason) {

        try {
            Payments payment = paymentsRepository.findByTossPaymentsKey(paymentKey)
                    .orElseThrow(); //예외추가해야함
            Long totalAmount = payment.getTotalAmount();

            Reservation reservation = paymentsRepository.findReservationByPayments(payment)
                    .orElseThrow(); //예외추가해야함

            reservation.changeReservationState(ReservationState.CANCELLED); //

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime reservationTime = reservation.getStartTime();
            return tossPaymentCancel(paymentKey,cancelReason,totalAmount);
        } catch (Exception e){
            throw e; //예외추가해야함
        }
    }

    public Map tossPaymentCancel(String paymentKey, String cancelReason, Long cancelAmount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();
        params.put("cancelReason", cancelReason);
        params.put("cancelAmount", cancelAmount);

        try {
            return restTemplate.postForObject(PaymentsConfig.URL +"/"+paymentKey+ "/cancel", //url이 안맞음 고쳐야함
                    new HttpEntity<>(params, headers),
                    Map.class);
        } catch (Exception e){
            throw e; //예외추가해야함
        }
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
                .orElseThrow(ErrorCode.RESERVATION_NOT_FOUND::commonException); // 예외 추가해야함

        // 예약 상태 검증
        if(reservation.getReservationState().equals(ReservationState.COMPLETED)){
            throw ErrorCode.RESERVATION_ALREADY_COMPLETED.commonException();
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
