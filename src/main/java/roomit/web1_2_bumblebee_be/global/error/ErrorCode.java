package roomit.web1_2_bumblebee_be.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    MEMBER_NOT_FOUND(400, "M003-1", "존재 하지 않는 회원입니다."),
    MEMBER_UPDATE_EXCEPTION(400, "M001", "잘못된 회원 수정 입니다.");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(int status,String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

}
