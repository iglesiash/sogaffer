package es.unican.hgi834.sogaffer.exception;

import es.unican.hgi834.sogaffer.model.dto.sorare.graphql.SorareGraphQLError;
import es.unican.hgi834.sogaffer.model.dto.sorare.graphql.SorareGraphQLErrorExtension;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SorareAuthException extends RuntimeException {

    public SorareAuthException(List<SorareGraphQLError> errors) {
        super(buildMessage(errors));
    }

    private static String buildMessage(List<SorareGraphQLError> errors) {
        if (errors == null || errors.isEmpty()) {
            return "Sorare authentication failed";
        }
        return errors.stream()
                .map(SorareGraphQLError::message)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }
}