package es.unican.hgi834.sogaffer.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDto(@NotBlank @NotNull String email,
                       @NotBlank @NotNull String password) {
}
