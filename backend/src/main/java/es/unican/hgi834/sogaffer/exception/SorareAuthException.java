package es.unican.hgi834.sogaffer.exception;

import es.unican.hgi834.sogaffer.model.dto.sorare.error.SorareError;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SorareAuthException extends RuntimeException {

    public SorareAuthException(List<? extends SorareError> errors) {
        super(errors == null || errors.isEmpty() ?
                "Sorare errors not found" :

                errors.stream()
                .map(SorareError::message)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", ")));
    }
}