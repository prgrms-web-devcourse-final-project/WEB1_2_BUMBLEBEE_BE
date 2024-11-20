package roomit.web1_2_bumblebee_be.global.error;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private final int status;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(ErrorCode errorCode, Map<String, String> validation) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.validation = validation;
    }
    @Getter
    public static class ErrorValidation{

        private final String code;
        private final String message;
        private  Map<String, String> validation;

        @Builder
        public ErrorValidation(String code, String message, Map<String, String> validation) {
            this.code = code;
            this.message = message;
            this.validation = validation != null ? validation : new HashMap<>();
        }

        public void addValidation(String fieldName, String errorMessage) {
            this.validation.put(fieldName, errorMessage);
        }
    }
}