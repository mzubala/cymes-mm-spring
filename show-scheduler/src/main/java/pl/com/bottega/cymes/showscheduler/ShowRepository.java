package pl.com.bottega.cymes.showscheduler;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.bottega.cymes.showscheduler.dto.ShowDto;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

interface ShowRepository extends JpaRepository<Show, Long> {
    boolean collidingShowsPresent(Instant ns, Instant ne, Long cinemaId, Long cinemaHallId);

    Stream<Show> findByCinemaIdAndFromAfterAndUntilBefore(Long cinemaId, Instant dayStart, Instant dayEnd);

    <T> Optional<T> findById(Long showId, Class<T> projection);
}
