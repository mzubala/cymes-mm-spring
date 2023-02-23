package pl.com.bottega.cymes.movies;

import com.google.common.collect.Iterables;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MoviesFacade {

    private final MovieRepository movieRepository;

    public MovieDto getMovie(Long movieId) {
        return movieRepository.findById(movieId, MovieDto.class).orElseThrow(EntityNotFoundException::new);
    }

    public List<MovieDto> getMovies(Iterable<Long> ids) {
        var movies = movieRepository.findByIdIn(ids, MovieDto.class);
        if(movies.size() != Iterables.size(ids)) {
            throw new EntityNotFoundException("At least one movie id is invalid");
        }
        return movies;
    }

    public record MovieDto(Long id, String title, Integer durationMinutes) {

    }

}
