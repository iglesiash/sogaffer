package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.graphql.SorareGraphQLResponse;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInWrapperDto;

public interface ISorareGraphQLService {
    SorareSignInWrapperDto signIn(LoginDto loginDto);
}
