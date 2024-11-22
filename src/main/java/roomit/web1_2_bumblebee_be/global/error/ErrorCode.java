package roomit.web1_2_bumblebee_be.global.error;

import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.review.exception.ReviewNotFound;

@Getter
public enum ErrorCode {
    /*Login*/
    BUSINESS_NOT_REGISTER(400, "L006", "사업자 등록에 실패했습니다."),

    /*Member*/
    MEMBER_NOT_FOUND(400, "M003-1", "존재 하지 않는 회원입니다."),
    MEMBER_UPDATE_EXCEPTION(400, "M001", "잘못된 회원 수정 입니다."),

    /*StudyRoom*/
    STUDYROOM_NOT_FOUND(400,"S001","존재하지 않는 스터디룸입니다."),

    /*Business*/
    BUSINESS_NOT_FOUND(400, "B003", "존재 하지 않는 사업자입니다."),
    BUSINESS_NOT_MODIFY(400, "B001", "사업자 수정에 실패했습니다."),
    BUSINESS_NOT_DELETE(400, "B002", "사업자 탈퇴에 실패했습니다."),

    /*WorkPlace*/
    WORKPLACE_NOT_REGISTERED(401, "W001","사업장 등록에 실패하였습니다."),
    WORKPLACE_NOT_MODIFIED(403,"W002","사업장 수정에 실패하였습니다."),
    WORKPLACE_NOT_DELETE(403,"W003","사업장 삭제에 실패하였습니다."),
    WORKPLACE_NOT_FOUND(400,"W004","존재하지 않는 사업장 입니다"),
    WORKPLACE_INVALID_REQUEST(400,"W001-2","잘못된 입력입니다."),


    /*Review*/
    REVIEW_NOT_FOUND(400,"V003","존재 하지 않는 리뷰입니다."),
    REVIEW_UPDATE_EXCEPTION(400,"V002","잘못 수정 입니다.");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(int status,String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

}
