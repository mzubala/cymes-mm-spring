package pl.com.bottega.cymes.cinemas;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

interface SuspensionRepository extends JpaRepository<Suspension, Long> {
    <P> List<P> findByCinemaOrderByFromAscUntilAsc(Class<P> projcetion, Cinema cinema);

    boolean existsByCinemaAndFromLessThanEqualAndUntilGreaterThanEqual(
        Cinema cinema, Instant fromLEq, Instant untilGEq
    );

    boolean existsByCinemaHallAndFromLessThanEqualAndUntilGreaterThanEqual(
        CinemaHall cinemaHall, Instant fromLEq, Instant untilGEq
    );

    <P> List<P> findByCinemaHallOrderByFromAscUntilAsc(Class<P> projcetion, CinemaHall cinemaHall);

    List<Suspension> findByCinemaHallInAndFromLessThanEqualAndUntilGreaterThanEqual(
        List<CinemaHall> halls, Instant fromLeq, Instant untilGeq
    );
}
