package es.unican.hgi834.sogaffer.model.dto.sorare.auth;

import es.unican.hgi834.sogaffer.model.dto.sorare.error.SorareApiErrorDto;

import java.util.List;

public record SorareSignInDto(SorareCurrentUserDto currentUser,
                              SorareJwtTokenDto jwtToken,
                              List<SorareApiErrorDto> errors) {
}