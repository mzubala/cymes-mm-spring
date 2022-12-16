package pl.com.bottega.cymes.cinemas;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@NoArgsConstructor
class Suspension {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @Column(name = "beginning")
    private Instant from;
    private Instant until;
    private Boolean active;

    @ManyToOne
    private Cinema cinema;

    @ManyToOne
    private CinemaHall cinemaHall;

    Suspension(Cinema cinema, Instant from, Instant until) {
        this.cinema = cinema;
        this.from = from;
        this.until = until;
    }

    public Suspension(CinemaHall cinemaHall, Instant from, Instant until) {
        this.cinemaHall = cinemaHall;
        this.from = from;
        this.until = until;
    }

    void cancel() {
        active = false;
    }
}
