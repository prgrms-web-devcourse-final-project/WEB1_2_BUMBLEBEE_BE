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
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.notification.dto.ResponseNotificationDto;
import roomit.main.domain.notification.dto.ResponseNotificationReservationDto;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.entity.NotificationType;
import roomit.main.domain.notification.service.NotificationService;
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
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.global.error.ErrorCode;

@Service
@RequiredArgsConstructor
public class PaymentsService {

    private final PaymentsRepository paymentsRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentsConfig paymentsConfig;
    private final NotificationService notificationService;

    /**
     * 결제 검증
     */

    @Transactional
    public PaymentValidationResponse requestPayment(Long reservationId, Long memberId, PaymentsRequest paymentsRequest) {

        validateReservationForPayment(reservationId, memberId, paymentsRequest); // 검증

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ErrorCode.RESERVATION_NOT_FOUND::commonException);

        Workplace workplace = reservation.getStudyRoom().getWorkPlace();

        alrim(workplace,"예약이 완료 되었습니다.", paymentsRequest.totalAmount());
        try {
            Payments payments = paymentsRequest.toEntity();
            payments.addReservation(reservation);
            paymentsRepository.save(payments);

            reservation.changeReservationState(ReservationState.ACTIVE); // 엔드타임이 지나면 컴플리트로 << 로직을 추가하고 봐야함

            reservationRepository.save(reservation);

            return payments.toDto(paymentsConfig.getSuccessUrl(), paymentsConfig.getFailUrl());
        } catch (Exception e) {
            throw ErrorCode.PAYMENTS_VALIDATION_FAILED.commonException();
        }

    }
    public void alrim(Workplace workplace, String content, Long price) {
        Business business = workplace.getBusiness();

        Notification notification = Notification.builder()
                .business(business)
                .notificationType(NotificationType.RESERVATION_CONFIRMED)
                .content(workplace.getWorkplaceName() + content)
                .build();

        ResponseNotificationReservationDto responseNotificationDto = ResponseNotificationReservationDto
                .builder()
                .notification(notification)
                .workplaceId(workplace.getWorkplaceId())
                .price(price)
                .build();

        notificationService.customNotifyReservation(
                business.getBusinessId(),
                responseNotificationDto
        );

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

            return restTemplate.postForObject(PaymentsConfig.URL+"/confirm",
                    new HttpEntity<>(params, headers),
                    PaymentsResponse.class);
    }

    /**
     * 결제 실패
     */
    @Transactional
    public PaymentsFailResponse tossPaymentFail(String code, String message, String orderId) {
        Payments payment = paymentsRepository.findByOrderId(orderId)
                .orElseThrow(ErrorCode.PAYMENTS_NOT_FOUND::commonException);

        Reservation reservation = paymentsRepository.findReservationByPayments(payment)
                .orElseThrow(ErrorCode.RESERVATION_NOT_FOUND::commonException);

        try {
            reservation.changeReservationState(ReservationState.PAYMENT_FAIL);
            payment.changePaySuccessYN(false);
            payment.changeFailReason(message);
        } catch (Exception e){
            throw ErrorCode.PAYMENTS_FAILED.commonException();
        }

        return PaymentsFailResponse.builder()
                .errorCode(code)
                .errorMessage(message)
                .orderId(orderId)
                .build();
    }
    /**
     * 결제 취소
     */
    @Transactional
    public Map cancelPayments(Long reservationId, String cancelReason) {

        Payments payment = paymentsRepository.findByReservation_ReservationId(reservationId)
                .orElseThrow(ErrorCode.PAYMENTS_NOT_FOUND::commonException);
        Long amount = payment.getTotalAmount();

        String paymentKey = payment.getTossPaymentsKey();

        Reservation reservation = paymentsRepository.findReservationByPayments(payment)
                .orElseThrow(ErrorCode.RESERVATION_NOT_FOUND::commonException);

        Long totalAmount = calculateRefundAmount(reservation, amount);

        try {
            reservation.changeReservationState(ReservationState.CANCELLED);
            reservationRepository.save(reservation);

            payment.changeCancelYN(true);
            payment.changeCancelReason(cancelReason);
            paymentsRepository.save(payment);
        } catch (Exception e){
            throw ErrorCode.PAYMENTS_CANCEL_FAILED.commonException();
        }

        return tossPaymentCancel(paymentKey,cancelReason,totalAmount);
    }

    /**
     * (토스)결제 취소 요청
     */
    public Map tossPaymentCancel(String paymentKey, String cancelReason, Long cancelAmount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();
        params.put("cancelReason", cancelReason);
        params.put("cancelAmount", cancelAmount);

        return restTemplate.postForObject(PaymentsConfig.URL +"/"+paymentKey+ "/cancel",
                new HttpEntity<>(params, headers),
                Map.class);
    }

    /**
     * 예약 및 결제 검증 로직
     */
    public void validateReservationForPayment(Long reservationId, Long memberId, PaymentsRequest paymentRequest) {

        // 예약 조회 및 소유권 검증
        Reservation reservation = reservationRepository.findFirstByIdAndMemberId(reservationId, memberId)
                .orElseThrow(ErrorCode.RESERVATION_NOT_FOUND::commonException);

        // 예약 상태 검증
        if(reservation.getReservationState().equals(ReservationState.COMPLETED)){
            throw ErrorCode.RESERVATION_ALREADY_COMPLETED.commonException();
        }

        // 결제 금액 검증
        if (paymentRequest.totalAmount() < 1000) {
            throw ErrorCode.PAYMENTS_INVALID_AMOUNT.commonException();
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
     * 환불 규정에 맞는지 검증
     */
    private Long calculateRefundAmount(Reservation reservation, Long totalAmount) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationTime = reservation.getStartTime();

        if (now.isBefore(reservationTime.minusDays(2))) {
            return totalAmount;
        } else if (now.isBefore(reservationTime.minusDays(1))) {
            return totalAmount / 2;
        } else {
            throw ErrorCode.RESERVATION_CANNOT_CANCEL.commonException();
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
