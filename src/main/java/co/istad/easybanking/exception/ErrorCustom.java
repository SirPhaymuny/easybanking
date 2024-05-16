package co.istad.easybanking.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import co.istad.easybanking.base.BaseError;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorCustom {
    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<BaseError<?>> handleServiceException(ResponseStatusException e) {
        BaseError<?> baseError = BaseError.builder()
                .errors(e.getReason())
                .status(false)
                .code(e.getStatusCode().value())
                .message("Something went wrong!")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(baseError, e.getStatusCode());
    }

}
