package pl.com.bottega.cymes.movies;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.movies.requests.CreateMovieRequest;
import pl.com.bottega.cymes.movies.requests.MovieDto;
import pl.com.bottega.cymes.movies.requests.UpdateMovieRequest;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@Log
class MoviesController {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final StarRepository starRepository;

    @PostMapping
    @Transactional
    void create(@RequestBody CreateMovieRequest request) {
        movieRepository.save(
            new Movie(
                 null,
                request.title(),
                request.description(),
                starRepository.getReferenceById(request.directorId()),
                getReferences(starRepository, request.actorIds()),
                getReferences(genreRepository, request.genreIds()),
                request.durationMinutes()
            )
        );
    }

    @PutMapping("/{id}")
    @Transactional
    void update(@PathVariable Long id, @RequestBody UpdateMovieRequest request) {
        var movie = movieRepository.getReferenceById(id);
        movie.setTitle(request.title());
        movie.setDescription(request.description());
        movie.setGenres(getReferences(genreRepository, request.genreIds()));
        movie.setActors(getReferences(starRepository, request.actorIds()));
        movie.setDirector(starRepository.getReferenceById(request.directorId()));
        movie.setDurationMinutes(request.durationMinutes());
    }

    @GetMapping
    Page<MovieDto> search(@RequestParam String phrase, Pageable pageable) {
        return movieRepository.findAll(MovieSpecifications.byPhrase(phrase), pageable).map(Movie::toDto);
    }

    private <T, I> Set<T> getReferences(JpaRepository<T, I> jpaRepository, Set<I> ids) {
        return ids.stream().map(jpaRepository::getReferenceById).collect(toSet());
    }
}
