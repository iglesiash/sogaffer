package es.unican.hgi834.sogaffer.model.dto.sorare.auth;

import java.util.List;

public record SorareSignInDto(SorareCurrentUserDto currentUser,
                              SorareJwtTokenDto jwtToken,
                              List<SorareApiErrorDto> errors) {
}