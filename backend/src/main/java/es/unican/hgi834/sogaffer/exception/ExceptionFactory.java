package es.unican.hgi834.sogaffer.exception;

import org.springframework.http.HttpStatus;

public class ExceptionFactory {

    /**
     * Creates a {@code SogafferErrorDto} object representing a "Bad Request" error with the
     * specified error message.
     *
     * @param message the error message to include in the response.
     * @return a {@code SogafferErrorDto} object with status code 400 (Bad Request),
     * an error description, and the provided message.
     */
    public static SogafferErrorDto badRequest(String message) {
        return new SogafferErrorDto(HttpStatus.BAD_REQUEST.value(), "Bad Request", message);
    }

    /**
     * Creates a {@code SogafferErrorDto} object representing an "Internal Server" error with the
     * specified error message.
     *
     * @param message the error message to include in the response.
     * @return a {@code SogafferErrorDto} object with status code 500 (Internal Server Error),
     * an error description, and the provided message.
     */
    public static SogafferErrorDto internalServerError(String message) {
        return new SogafferErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error", message);
    }

    /**
     * Creates a {@code SogafferErrorDto} object representing an "Unauthorized" error with the
     * specified error message.
     *
     * @param message the error message to include in the response.
     * @return a {@link SogafferErrorDto} object with status code 401 (Unauthorized),
     * an error description, and the provided message.
     */
    public static SogafferErrorDto unauthorized(String message) {
        return new SogafferErrorDto(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", message);
    }
}
