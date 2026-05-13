package es.unican.hgi834.sogaffer.model.dto.sorare.auth;

import java.time.Instant;

public record SorareJwtTokenDto(String token,
                                Instant expiredAt) {
}