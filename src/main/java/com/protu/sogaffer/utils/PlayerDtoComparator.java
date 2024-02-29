package com.protu.sogaffer.utils;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.protu.sogaffer.dto.players.PlayerDto;

public class PlayerDtoComparator implements Comparator<PlayerDto> {

    private Collator collator;

    public PlayerDtoComparator() {
        this.collator = Collator.getInstance(new Locale("en", "US"));
        this.collator.setStrength(Collator.SECONDARY);
    }

    @Override
    public int compare(PlayerDto player, PlayerDto other) {
        // Check if both labels are null
        if (player.getLabel() == null && other.getLabel() == null) {
            return 0; // Consider them equal if both labels are null
        }
        // Check if one of the labels is null
        if (player.getLabel() == null) {
            return -1; // Consider any non-null label greater than null
        }
        if (other.getLabel() == null) {
            return 1; // Similarly, consider any non-null label greater than null
        }
        // If neither label is null, proceed with the usual comparison
        return collator.compare(player.getLabel(), other.getLabel());
    }

}
