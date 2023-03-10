package pl.com.bottega.cymes.movies;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.bottega.cymes.movies.requests.MovieDto;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    private Star director;

    @ManyToMany
    private Set<Star> actors;

    @ManyToMany
    private Set<Genre> genres;

    private Integer durationMinutes;

    MovieDto toDto() {
        return new MovieDto(id, title, description, genres.stream().map(Genre::toDto).collect(Collectors.toSet()),
            director.toDto(), actors.stream().map(Star::toDto).collect(Collectors.toList()), durationMinutes
        );
    }
}
