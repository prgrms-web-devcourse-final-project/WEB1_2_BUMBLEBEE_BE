package roomit.web1_2_bumblebee_be.global.error;

public enum ErrorCode {
    MEMBER_NOT_FOUND(400, "M001", "존재 하지 않는 회원입니다.");


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
