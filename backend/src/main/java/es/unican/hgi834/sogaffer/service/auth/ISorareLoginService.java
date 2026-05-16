package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.entity.User;

public interface ISorareLoginService {
    User login(LoginDto loginDto);
}
