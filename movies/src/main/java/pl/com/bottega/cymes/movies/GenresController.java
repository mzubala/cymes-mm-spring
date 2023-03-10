package pl.com.bottega.cymes.movies;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
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

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Log
class GenresController {

    private final GenreRepository genreRepository;

    @PostMapping
    void create(@Valid @RequestBody CreateGenreRequest request) {
        genreRepository.save(new Genre(null, request.name()));
    }

    @PutMapping
    @RequestMapping("/{genreId}")
    @Transactional
    public void update(@PathVariable Long genreId, @RequestBody @Valid UpdateGenreRequest request) {
        var genre = genreRepository.getReferenceById(genreId);
        genre.setName(request.name());
        genreRepository.save(genre);
    }

    @GetMapping
    List<GenreDto> getAll() {
        return genreRepository.findAll(Sort.by("name")).stream().map(Genre::toDto).collect(toList());
    }
}
