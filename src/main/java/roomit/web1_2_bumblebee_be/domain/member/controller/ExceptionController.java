package roomit.web1_2_bumblebee_be.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomit.web1_2_bumblebee_be.domain.member.exception.commonException;
import roomit.web1_2_bumblebee_be.domain.member.dto.response.ErrorResponse;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(commonException.class)
    public ResponseEntity<ErrorResponse> commonException(commonException e) {
        int statusCode = e.getStatusCode();
        ErrorResponse response = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(statusCode).body(response);
    }
}
