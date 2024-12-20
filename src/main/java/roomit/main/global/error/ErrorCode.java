package roomit.main.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import roomit.main.global.exception.CommonException;

@Getter
public enum ErrorCode {
    /*Login*/
    BUSINESS_NOT_REGISTER(HttpStatus.BAD_REQUEST, "L006", "사업자 등록에 실패했습니다."),

    /*Member*/
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "M003-1", "존재 하지 않는 회원입니다."),
    MEMBER_UPDATE_EXCEPTION(HttpStatus.BAD_REQUEST, "M001", "잘못된 회원 수정 입니다."),
    MEMBER_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "L005", "중복된 이메일 입니다."),
    MEMBER_DUPLICATE_NICK_NAME(HttpStatus.BAD_REQUEST, "L006", "중복된 닉네임 입니다."),

    /*Reservation*/
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST,"R001","존재하지 않는 예약입니다."),
    RESERVATION_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "R002", "이미 완료된 예약입니다."),
    START_TIME_NOT_AFTER_END_TIME(HttpStatus.BAD_REQUEST,"R003","종료시간이 시작시간보다 빠릅니다."),
    RESERVATION_NOT_MODIFIED(HttpStatus.NOT_MODIFIED,"R004","예약 수정이 완료되지 않았습니다."),
    RESERVATION_IS_EMPTY(HttpStatus.NOT_FOUND,"R005","존재하는 예약이 없습니다."),
    RESERVATION_CANNOT_CANCEL(HttpStatus.BAD_REQUEST,"R006","예약취소는 당일에는 불가능합니다.."),
    DUPLICATE_RESERVATION(HttpStatus.BAD_REQUEST,"R007","예약 시간이 겹칩니다."),
    RESERVATION_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "R008", "취소된 예약입니다."),

    /*StudyRoom*/
    STUDYROOM_NOT_FOUND(HttpStatus.NOT_FOUND,"S001","존재하지 않는 스터디룸입니다."),
    NOT_OWNER_OF_STUDYROOM(HttpStatus.FORBIDDEN, "S002", "본인이 소유한 사업장이 아닙니다."),
    STUDYROOM_NOT_REGISTERD(HttpStatus.BAD_REQUEST, "S003","스터디룸 등록에 실패하였습니다."),
    STUDYROOM_NOT_MODIFY(HttpStatus.BAD_REQUEST, "S004","스터디룸 수정에 실패하였습니다."),
    STUDYROOM_NOT_DELETE(HttpStatus.BAD_REQUEST, "S004","스터디룸 삭제에 실패하였습니다."),

    /*Business*/
    BUSINESS_NOT_FOUND(HttpStatus.BAD_REQUEST, "B003", "존재 하지 않는 사업자입니다."),
    BUSINESS_NOT_MODIFY(HttpStatus.BAD_REQUEST, "B001", "사업자 수정에 실패했습니다."),
    BUSINESS_NOT_DELETE(HttpStatus.BAD_REQUEST, "B002", "사업자 탈퇴에 실패했습니다."),
    BUSINESS_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "B003", "권한이 없습니다."),
    BUSINESS_EMAIL_DUPLICATION(HttpStatus.CONFLICT, "B004", "이미 등록된 이메일입니다."),
    BUSINESS_NUMBER_DUPLICATION(HttpStatus.CONFLICT, "B005", "이미 등록된 사업자번호입니다."),
    BUSINESS_NICKNAME_DUPLICATION(HttpStatus.CONFLICT, "B006", "이미 등록된 사업자이름입니다."),

    /*WorkPlace*/
    WORKPLACE_NOT_REGISTERED(HttpStatus.UNAUTHORIZED, "W001","사업장 등록에 실패하였습니다."),
    WORKPLACE_NOT_MODIFIED(HttpStatus.FORBIDDEN,"W002","사업장 수정에 실패하였습니다."),
    WORKPLACE_NOT_DELETE(HttpStatus.FORBIDDEN,"W003","사업장 삭제에 실패하였습니다."),
    WORKPLACE_NOT_FOUND(HttpStatus.NOT_FOUND,"W004","존재하지 않는 사업장 입니다"),
    WORKPLACE_INVALID_REQUEST(HttpStatus.BAD_REQUEST,"W001-2","잘못된 입력입니다."),
    WORKPLACE_INVALID_ADDRESS(HttpStatus.BAD_REQUEST,"W001-3","잘못된 주소입니다."),
    WORKPLACE_RECOMMEND_FAIL(HttpStatus.BAD_REQUEST,"W005","맞춤형 추천에 실패하였습니다."),
    PYTHON_CONNECTED_FAIL(HttpStatus.BAD_REQUEST,"W006","파이썬 서버와 통신에 실패하였습니다."),

    /*Review*/
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST,"V003","존재 하지 않는 리뷰입니다."),
    REVIEW_UPDATE_EXCEPTION(HttpStatus.BAD_REQUEST,"V002","잘못 수정 입니다."),
    REVIEW_UPDATE_FAIL(HttpStatus.BAD_REQUEST,"V003", "본인 예약 정보가 아닙니다."),

    /*Payments*/
    PAYMENTS_NOT_FOUND(HttpStatus.NOT_FOUND,"P001","존재 하지 않는 결제 내역 입니다."),
    PAYMENTS_INVALID_AMOUNT(HttpStatus.BAD_REQUEST,"P002","잘못된 결제 금액입니다."),
    PAYMENTS_ALREADY_APPROVED(HttpStatus.BAD_REQUEST,"P003","이미 승인된 결제입니다"),
    PAYMENTS_AMOUNT_EXP(HttpStatus.BAD_REQUEST,"P004","결제 금액이 일치하지 않습니다"),
    PAYMENTS_PROCESS_FAILED(HttpStatus.BAD_REQUEST, "P005", "결제 처리에 실패했습니다."),
    PAYMENTS_CANCEL_FAILED(HttpStatus.BAD_REQUEST, "P006", "결제 취소에 실패했습니다."),
    PAYMENTS_FAILED(HttpStatus.BAD_REQUEST, "P007", "결제 실패했습니다."),
    PAYMENTS_VALIDATION_FAILED(HttpStatus.BAD_REQUEST,"P008","결제 검증에 실패했습니다"),

    /*OAuth*/
    OAUTH_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "O001", "OAuth 로그인에 실패했습니다."),

    /*S3*/
    S3_IMAGE_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "S3에서 이미지를 가져오는 데 실패했습니다."),
    S3_IMAGE_NOT_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "S002","S3에서 폴더를 삭제하는데 실패했습니다."),

    /*Login*/
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "L001", "로그인에 실패했습니다."),

    /*Token*/
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "T001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "T002", "토큰이 만료되었습니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "T003", "잘못된 형식의 토큰입니다."),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "T004", "토큰이 누락되었습니다."),
    TOKEN_VERIFICATION_FAILED(HttpStatus.UNAUTHORIZED, "T005", "토큰 검증에 실패했습니다."),
    INSUFFICIENT_ROLE(HttpStatus.FORBIDDEN, "T006", "권한이 부족합니다."),

    /*Notice*/
    SUBSCRIBE_FAIL(HttpStatus.BAD_REQUEST, "N001", "구독 연결에 실패했습니다."),


    /*Chat*/
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND,"C001","존재하지 채팅방 입니다"),
    CHAT_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "C002", "채팅 권한이 없습니다."),
;
    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(HttpStatus status,String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
    public CommonException commonException() {
        return new CommonException(this);
    }
}
