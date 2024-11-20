package roomit.web1_2_bumblebee_be.global.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    MEMBER_NOT_FOUND(400, "M001", "존재 하지 않는 회원입니다."),
    WORKPLACE_NOT_REGISTERED(401, "W001","사업장 등록에 실패하였습니다."),
    WORKPLACE_NOT_MODIFIED(403,"W002","사업장 수정에 실패하였습니다."),
    WORKPLACE_NOT_DELETE(403,"W003","사업장 삭제에 실패하였습니다."),
    WORKPLACE_NOT_FOUND(400,"W004","존재하지 않는 사업장 입니다"),
    WORKPLACE_INVALID_REQUEST(400,"E001","잘못된 입력입니다."),

    ;

    private  String code;
    private  String message;
    private  int status;

    ErrorCode(int status,String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
