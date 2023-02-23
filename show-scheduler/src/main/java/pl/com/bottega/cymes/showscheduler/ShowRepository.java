package pl.com.bottega.cymes.showscheduler;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.stream.Stream;

interface ShowRepository extends JpaRepository<Show, Long> {
    boolean collidingShowsPresent(Instant ns, Instant ne, Long cinemaId, Long cinemaHallId);

    Stream<Show> findByCinemaIdAndFromAfterAndUntilBefore(Long cinemaId, Instant dayStart, Instant dayEnd);
}
