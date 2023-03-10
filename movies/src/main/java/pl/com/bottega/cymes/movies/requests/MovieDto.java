package pl.com.bottega.cymes.movies.requests;

import pl.com.bottega.cymes.movies.dto.GenreDto;
import pl.com.bottega.cymes.movies.dto.StarDto;

import java.util.List;
import java.util.Set;

public record MovieDto(
    Long id, String title, String description, Set<GenreDto> genres, StarDto director, List<StarDto> actors,
    Integer durationMinutes
) {
}
