package pl.com.bottega.cymes.showscheduler.dto;

import java.time.Instant;

public record ShowDto(Long id, Long cinemaId, Long cinemaHallId, Instant from) {

    public Long showId() {
        return id;
    }

    public Instant when() {
        return from;
    }

}
