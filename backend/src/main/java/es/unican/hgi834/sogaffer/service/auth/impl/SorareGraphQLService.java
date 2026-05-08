package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.exception.SorareAuthException;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.graphql.SorareGraphQLError;
import es.unican.hgi834.sogaffer.model.dto.sorare.graphql.SorareGraphQLResponse;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInWrapperDto;
import es.unican.hgi834.sogaffer.service.auth.ISorareGraphQLService;
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
    public SorareSignInWrapperDto signIn(LoginDto loginDto) {
        String signInMutation = """
                mutation SignInMutation($input: signInInput!) {
                  signIn(input: $input) {
                    currentUser {
                      slug
                    }
                    jwtToken(aud: "%s") {
                      token
                      expiredAt
                    }
                    errors {
                      message
                    }
                  }
                }
                """.formatted("SoGaffer");

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

        return response.data();
    }
}
