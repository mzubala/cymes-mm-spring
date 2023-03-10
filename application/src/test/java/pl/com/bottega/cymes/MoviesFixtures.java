package pl.com.bottega.cymes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.movies.requests.CreateGenreRequest;
import pl.com.bottega.cymes.movies.requests.CreateMovieRequest;
import pl.com.bottega.cymes.movies.requests.CreateStarRequest;

import java.util.Set;

import static pl.com.bottega.cymes.commons.test.MockMvcAssertions.assertSuccess;

@Component
@RequiredArgsConstructor
class MoviesFixtures {

    private final MoviesApi moviesApi;

    Long batmanId;
    Long godfellasId;

    void create() {
        createGenres();
        createStars();
        createMovies();
    }

    private void createStars() {
        assertSuccess(moviesApi.createStar(new CreateStarRequest("Christian", null, "Bale")),
            moviesApi.createStar(new CreateStarRequest("Robert", null, "DeNiro")),
            moviesApi.createStar(new CreateStarRequest("Joe", null, "Pesci")),
            moviesApi.createStar(new CreateStarRequest("Martin", null, "Scorsese")),
            moviesApi.createStar(new CreateStarRequest("Christopher", null, "Nolan"))
        );
    }

    private void createGenres() {
        assertSuccess(moviesApi.createGenre(new CreateGenreRequest("Drama")),
            moviesApi.createGenre(new CreateGenreRequest("Comedy")),
            moviesApi.createGenre(new CreateGenreRequest("Thriller"))
        );
    }

    private void createMovies() {
        assertSuccess(moviesApi.createMovie(new CreateMovieRequest("Batman", "test description",
            Set.of(moviesApi.getGenreId("Drama"), moviesApi.getGenreId("Thriller")), moviesApi.getStarId("Nolan"),
            Set.of(moviesApi.getStarId("Bale")), 120
        )), moviesApi.createMovie(
            new CreateMovieRequest("The Godfellas", "test description", Set.of(moviesApi.getGenreId("Drama")),
                moviesApi.getStarId("Scorsese"), Set.of(moviesApi.getStarId("DeNiro"), moviesApi.getStarId("Pesci")),
                113
            )));
        batmanId = moviesApi.getMovie("Batman").id();
        godfellasId = moviesApi.getMovie("The Godfellas").id();
    }
}
