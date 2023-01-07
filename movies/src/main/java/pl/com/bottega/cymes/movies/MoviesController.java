package pl.com.bottega.cymes.movies;

import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.movies.dto.GenreDto;
import pl.com.bottega.cymes.movies.dto.StarDto;
import pl.com.bottega.cymes.movies.requests.CreateMovieRequest;
import pl.com.bottega.cymes.movies.requests.MovieDto;
import pl.com.bottega.cymes.movies.requests.UpdateMovieRequest;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/movies")
@Log
class MoviesController {
    @PostMapping
    void create(@RequestBody CreateMovieRequest request) {
        log.info("Create movie " + request);
    }

    @PutMapping("/{id}")
    void update(@PathVariable Long id, @RequestBody UpdateMovieRequest request) {
        log.info(String.format("Update movie %d %s", id, request));
    }

    @GetMapping
    Page<MovieDto> search(@RequestParam String phrase, Pageable pageable) {
        return new PageImpl<>(
            List.of(new MovieDto(1L,
                "The Godfellas",
                "XXX",
                Set.of(new GenreDto(1L, "Dramat")),
                new StarDto(1L, "Martin", "Scorsese"),
                Set.of(new StarDto(2L, "Robert", "Deniro")))
            ),
            pageable,
            1L
        );
    }
}
