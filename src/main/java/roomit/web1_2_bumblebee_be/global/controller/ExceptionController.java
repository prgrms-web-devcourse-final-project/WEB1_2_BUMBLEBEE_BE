package roomit.web1_2_bumblebee_be.global.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomit.web1_2_bumblebee_be.global.error.ErrorResponse;
import roomit.web1_2_bumblebee_be.global.exception.commonException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(commonException.class)
    public ResponseEntity<ErrorResponse> commonException(commonException e) {
        int statusCode = e.getStatusCode();
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .build();

        return ResponseEntity.status(statusCode).body(response);
    }
}
