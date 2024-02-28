package com.protu.sogaffer.dto.players;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerDto {
    private String slug;
    private String displayName;
    private String squaredPictureUrl;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof PlayerDto)) {
            return false;
        }

        PlayerDto that = (PlayerDto) obj;

        return this.slug.equals(that.getSlug());
    } // equals

    @Override
    public int hashCode() {
        return Objects.hash(slug);
    } // hashCode

}
