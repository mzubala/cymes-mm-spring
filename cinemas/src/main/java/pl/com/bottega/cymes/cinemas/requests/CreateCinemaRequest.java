package pl.com.bottega.cymes.cinemas.requests;

import jakarta.validation.constraints.NotBlank;

public record CreateCinemaRequest(@NotBlank String city, @NotBlank String name) {
}
