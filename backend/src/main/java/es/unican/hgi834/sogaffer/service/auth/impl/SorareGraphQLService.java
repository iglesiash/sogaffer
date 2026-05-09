package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.exception.SorareAuthException;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.error.SorareGraphQLError;
import es.unican.hgi834.sogaffer.model.dto.sorare.graphql.SorareGraphQLResponse;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInWrapperDto;
import es.unican.hgi834.sogaffer.service.auth.ISorareGraphQLService;
import es.unican.hgi834.sogaffer.utils.GraphQLQueryLoader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class SorareGraphQLService implements ISorareGraphQLService {

    private final RestClient restClient;

    public SorareGraphQLService(@Qualifier("graphQLClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public SorareSignInDto signIn(LoginDto loginDto) {
        String signInMutation = GraphQLQueryLoader.getSignInMutation();

        Map<String, Object> variables = Map.of("input", loginDto);

        Map<String, Object> body = Map.of(
                "operationName", "SignInMutation",
                "query", signInMutation,
                "variables", variables
        );

        SorareGraphQLResponse<SorareSignInWrapperDto> response = restClient
                .post()
                .body(body)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (response == null || !response.isSuccess()) {
            List<SorareGraphQLError> errors = response != null ? response.errors() : List.of();
            throw new SorareAuthException(errors);
        }

        SorareSignInDto signInData = response.data().signIn();
        if (!signInData.errors().isEmpty()) {
            throw new SorareAuthException(signInData.errors());
        }

        return response.data().signIn();
    }
}
