package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.entity.User;

public interface IRefreshTokenService {
    String generateRefreshToken(User user);
}
