package es.unican.hgi834.sogaffer.exception;

import org.springframework.http.HttpStatus;

public class ExceptionFactory {

    public static SogafferErrorDto badRequest(String message) {
        return new SogafferErrorDto(HttpStatus.BAD_REQUEST.value(), "Bad Request", message);
    }

    public static SogafferErrorDto internalServerError(String message) {
        return new SogafferErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error", message);
    }

    public static SogafferErrorDto unauthorized(String message) {
        return new SogafferErrorDto(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", message);
    }
}
