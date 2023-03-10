package pl.com.bottega.cymes.movies.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateMovieRequest(
    @NotBlank String title, @NotBlank String description, @NotEmpty Set<Long> genreIds, @NotNull Long directorId,
    @NotEmpty Set<Long> actorIds, @NotNull @Min(1) Integer durationMinutes
) {
}
