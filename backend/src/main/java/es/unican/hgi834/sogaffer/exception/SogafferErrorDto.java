package es.unican.hgi834.sogaffer.exception;

import java.time.Instant;

public record SogafferErrorDto(Instant timestamp,
                              int status,
                              String error,
                              String message) {
    public SogafferErrorDto(int status,
                            String error,
                            String message) {
        this(Instant.now(), status, error, message);
    }
}