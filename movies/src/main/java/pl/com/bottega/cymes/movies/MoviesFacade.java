package pl.com.bottega.cymes.movies;

import org.springframework.lang.NonNull;

import java.util.List;

public class MoviesFacade {

    MovieDto getMovie(Long movieId) {
        // TODO
        return null;
    }

    List<MovieDto> getMovies(Iterable<Long> ids) {
        // TODO
        return List.of();
    }

    public record MovieDto(Long movieId, String title, Integer durationMinutes) {

    }

}
