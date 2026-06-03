package es.unican.hgi834.sogaffer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SorareAuthException.class)
    public ResponseEntity<SogafferErrorDto> handleInvalidLogin(SorareAuthException e) {
        SogafferErrorDto error = ExceptionFactory.unauthorized(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SogafferErrorDto> handleAnyOtherException(Exception e) {

        SogafferErrorDto error = ExceptionFactory.internalServerError(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}