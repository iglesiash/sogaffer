package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareCurrentUserDto;

public interface IUserService {
    UserDto getUserBySorareSignInDto(SorareCurrentUserDto currentUser);
}
