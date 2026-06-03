package es.unican.hgi834.sogaffer.model.dto.auth;

import java.time.Instant;
import java.util.UUID;

public record UserDto(int id,
                      UUID sorareId,
                      String email,
                      Instant updatedAt) {
}
