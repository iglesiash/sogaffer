package com.protu.sogaffer.dto.players;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerDto {
    @JsonAlias("slug")
    private String value;

    @JsonAlias("displayName")
    private String label;

    private String firstName;
    private String lastName;
    private Date birthDate;
    private Integer age;
    private String position;
    private ClubDto activeClub;
    private CountryDto country;
    private Integer shirtNumber;
    private String bestFoot;
    private Integer height;
    private Integer weight;
    private String playingStatus;
    private List<SuspensionDto> suspensions;
    private List<InjuryDto> injuries;
    private String squaredPictureUrl;
    private String pictureUrl;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof PlayerDto)) {
            return false;
        }

        PlayerDto that = (PlayerDto) obj;

        return this.value.equals(that.getValue());
    } // equals

    @Override
    public int hashCode() {
        return Objects.hash(value);
    } // hashCode

}
