package roomit.web1_2_bumblebee_be.domain.member.response;

import lombok.Builder;
import lombok.Getter;
import roomit.web1_2_bumblebee_be.global.error.ErrorCode;

import java.util.Map;

@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private int status;


    @Builder
    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }

}