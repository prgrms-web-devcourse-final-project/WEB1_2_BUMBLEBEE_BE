package roomit.web1_2_bumblebee_be.global.exception;


import lombok.Getter;
import roomit.web1_2_bumblebee_be.global.error.ErrorCode;


@Getter
public class CommonException extends RuntimeException{

    private final ErrorCode errorCode;

    public CommonException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
