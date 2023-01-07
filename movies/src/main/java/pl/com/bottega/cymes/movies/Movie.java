package pl.com.bottega.cymes.movies;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    private Star director;

    @ManyToMany
    private List<Star> actors;

    @ManyToMany
    private Set<Genre> genres;

    private Integer durationMinutes;
}
