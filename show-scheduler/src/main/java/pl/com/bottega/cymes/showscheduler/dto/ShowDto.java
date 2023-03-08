package pl.com.bottega.cymes.showscheduler.dto;

import java.time.Instant;

public record ShowDto(Long showId, Long cinemaId, Long cinemaHallId, Instant when) {
}
