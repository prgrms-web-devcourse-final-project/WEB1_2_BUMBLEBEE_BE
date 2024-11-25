package roomit.main.global.exception;


import lombok.Getter;
import roomit.main.global.error.ErrorCode;


@Getter
public class CommonException extends RuntimeException{

    private final ErrorCode errorCode;

    public CommonException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
