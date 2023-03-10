package pl.com.bottega.cymes.showscheduler;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

@Entity
@NamedQuery(name = "Show.collidingShowsPresent", query = "SELECT (count(s) > 0) FROM Show s WHERE "
    + "((:ns <= s.from AND :ne >= s.until AND :ne <= s.until) OR " + "(:ns <= s.from AND :ne >= s.until)  OR"
    + "(:ns >= s.from AND :ne <= s.until) OR " + "(:ns >= s.from AND :ns <= s.until AND :ne >= s.until)) AND "
    + "s.cinemaId = :cinemaId AND " + "s.cinemaHallId = :cinemaHallId")
class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long movieId;
    private Long cinemaId;
    private Long cinemaHallId;
    private Instant from;
    private Instant until;

    protected Show() {
    }

    Show(Long movieId, Long cinemaId, Long cinemaHallId, Instant from, Instant until) {
        this.movieId = movieId;
        this.cinemaId = cinemaId;
        this.cinemaHallId = cinemaHallId;
        this.from = from;
        this.until = until;
    }

    Instant getFrom() {
        return from;
    }

    Instant getUntil() {
        return until;
    }

    Long getMovieId() {
        return movieId;
    }

    Long getId() {
        return id;
    }

    LocalTime getStartTime() {
        return LocalTime.ofInstant(from, ZoneId.systemDefault());
    }
}
