package pl.com.bottega.cymes.showscheduler.dto;

import java.time.LocalTime;
import java.util.List;

public record ShowsGroupDto(
    MovieDto movie, List<ShowDto> shows
) {
    public record ShowDto(Long showId, LocalTime time) {
    }

    public record MovieDto(Long id, String title) {
    }
}


