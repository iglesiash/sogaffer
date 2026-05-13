package es.unican.hgi834.sogaffer.model.dto.sorare.error;

public record SorareGraphQLError(String message,
                                 SorareGraphQLErrorExtension extensions) implements SorareError {
}