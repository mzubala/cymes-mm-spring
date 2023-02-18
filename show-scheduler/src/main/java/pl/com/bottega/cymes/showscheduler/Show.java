package pl.com.bottega.cymes.showscheduler;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long movieId;
    private Long cinemaId;
    private Long cinemaHallId;
    private Instant from;
    private Instant until;

    protected Show() {}

    Show(Long id, Long movieId, Long cinemaId, Long cinemaHallId, Instant from, Instant until) {
        this.id = id;
        this.movieId = movieId;
        this.cinemaId = cinemaId;
        this.cinemaHallId = cinemaHallId;
        this.from = from;
        this.until = until;
    }
}
