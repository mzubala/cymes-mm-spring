package pl.com.bottega.cymes.showscheduler.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ScheduleShowRequest(
    @NotNull Long cinemaId, @NotNull Long cinemaHallId, @NotNull Long movieId, @NotNull @Future Instant when
) {
}
