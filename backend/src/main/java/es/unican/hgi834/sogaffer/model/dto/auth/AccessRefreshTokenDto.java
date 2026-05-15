package es.unican.hgi834.sogaffer.model.dto.auth;

public record AccessRefreshTokenDto(AccessTokenDto accessToken, String refreshToken) {}