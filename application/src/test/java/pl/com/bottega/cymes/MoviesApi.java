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

    private List<StarDto> stars;
    private List<GenreDto> genres;

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
        if (genres == null) {
            genres = getList("/genres", GenreDto.class);
        }
        return genres;
    }

    @SneakyThrows
    List<StarDto> getStars() {
        if (stars == null) {
            stars = getPage("/stars", StarDto.class).getContent();
        }
        return stars;
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
