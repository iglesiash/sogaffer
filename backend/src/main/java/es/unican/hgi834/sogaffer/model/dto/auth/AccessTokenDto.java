package es.unican.hgi834.sogaffer.model.dto.auth;

public record AccessTokenDto(String accessToken, int expiresIn) {
}