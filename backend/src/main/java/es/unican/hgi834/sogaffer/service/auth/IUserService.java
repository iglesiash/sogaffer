package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareCurrentUserDto;
import es.unican.hgi834.sogaffer.model.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface IUserService {
    User getUserBySorareSignInDto(SorareCurrentUserDto currentUser);
}
