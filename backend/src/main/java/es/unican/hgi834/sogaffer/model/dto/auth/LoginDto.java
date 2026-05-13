package es.unican.hgi834.sogaffer.model.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(@NotBlank String email, @NotBlank String password) {
}
