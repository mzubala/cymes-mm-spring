package pl.com.bottega.cymes.cinemas;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface CinemaHallRepository extends JpaRepository<CinemaHall, Long> {
    List<CinemaHall> findByCinema(Cinema cinema);
}
