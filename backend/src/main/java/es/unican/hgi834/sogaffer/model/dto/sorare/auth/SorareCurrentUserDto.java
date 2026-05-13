package es.unican.hgi834.sogaffer.model.dto.sorare.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SorareCurrentUserDto(@JsonProperty("id") String sorareId, String email) {
}