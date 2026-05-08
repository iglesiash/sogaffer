package es.unican.hgi834.sogaffer.model.dto.sorare.graphql;

import java.util.List;

public record SorareGraphQLResponse<T>(T data,
                                       List<SorareGraphQLError> errors) {

    public boolean isSuccess() {
        return hasData() && !hasErrors();
    }

    private boolean hasData() {
        return data != null;
    }

    private boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
}