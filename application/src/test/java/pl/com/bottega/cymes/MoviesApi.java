package pl.com.bottega.cymes;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.com.bottega.cymes.movies.dto.GenreDto;
import pl.com.bottega.cymes.movies.dto.StarDto;
import pl.com.bottega.cymes.movies.requests.CreateGenreRequest;
import pl.com.bottega.cymes.movies.requests.CreateMovieRequest;
import pl.com.bottega.cymes.movies.requests.CreateStarRequest;
import pl.com.bottega.cymes.movies.requests.MovieDto;

import java.util.List;

@Component
class MoviesApi extends Api {

    MoviesApi(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    @SneakyThrows
    ResultActions createGenre(CreateGenreRequest request) {
        return post("/genres", request);
    }

    ResultActions createStar(CreateStarRequest request) {
        return post("/stars", request);
    }

    @SneakyThrows
    ResultActions createMovie(CreateMovieRequest request) {
        return post("/movies", request);
    }

    List<GenreDto> getGenres() {
        return getList("/genres", GenreDto.class);
    }

    @SneakyThrows
    List<StarDto> getStars() {
        return getPage("/stars", StarDto.class).getContent();
    }

    Long getGenreId(String name) {
        return getGenres().stream().filter(g -> g.name().equals(name)).findFirst().orElseThrow(IllegalArgumentException::new).id();
    }

    Long getStarId(String surname) {
        return getStars().stream().filter(s -> s.lastName().equals(surname)).findFirst().orElseThrow(IllegalArgumentException::new).id();
    }

    @SneakyThrows
    MovieDto getMovie(String title) {
        return getPage("/movies?phrase={phrase}", MovieDto.class, title).getContent().get(0);
    }
}
