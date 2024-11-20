package roomit.web1_2_bumblebee_be.global.exception;

import lombok.Getter;
import roomit.web1_2_bumblebee_be.global.error.ErrorCode;

@Getter
public abstract class commonException extends RuntimeException{

    private  ErrorCode errorCode;

    public commonException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public abstract int getStatusCode();

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
