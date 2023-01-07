package pl.com.bottega.cymes.movies.requests;

import jakarta.validation.constraints.NotBlank;

public record CreateGenreRequest(@NotBlank String name) {
}
