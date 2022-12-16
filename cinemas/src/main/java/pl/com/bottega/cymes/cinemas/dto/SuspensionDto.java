package pl.com.bottega.cymes.cinemas.dto;

import java.time.Instant;

public record SuspensionDto(Long id, Instant from, Instant until) {
}
