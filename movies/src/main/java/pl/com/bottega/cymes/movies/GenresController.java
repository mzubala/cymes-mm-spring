package pl.com.bottega.cymes.movies;

import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.movies.dto.GenreDto;
import pl.com.bottega.cymes.movies.requests.CreateGenreRequest;
import pl.com.bottega.cymes.movies.requests.UpdateGenreRequest;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Log
class GenresController {
    @PostMapping
    void create(@RequestBody CreateGenreRequest request) {
        log.info(String.format("Create genre %s", request));
    }

    @PutMapping
    @RequestMapping("/{genreId}")
    void update(@PathVariable Long genreId, UpdateGenreRequest request) {
        log.info(String.format("Update genre %d %s", genreId, request));
    }

    @GetMapping
    List<GenreDto> getAll() {
        return List.of(
            new GenreDto(1L, "Komedia"),
            new GenreDto(2L, "Akcja")
        );
    }
}
