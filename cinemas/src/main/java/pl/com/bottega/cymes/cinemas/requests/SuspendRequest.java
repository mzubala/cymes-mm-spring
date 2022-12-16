package pl.com.bottega.cymes.cinemas.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record SuspendRequest(@NotNull @Future Instant from, @NotNull @Future Instant until) {
}
