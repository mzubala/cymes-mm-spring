package pl.com.bottega.cymes.movies.requests;

import jakarta.validation.constraints.NotBlank;

public record CreateStarRequest(
    @NotBlank String firstName,
    @NotBlank String lastName
) {
}
