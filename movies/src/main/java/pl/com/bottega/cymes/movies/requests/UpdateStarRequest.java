package pl.com.bottega.cymes.movies.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateStarRequest(
    @NotBlank String firstName,
    @NotBlank String lastName
) {
}
