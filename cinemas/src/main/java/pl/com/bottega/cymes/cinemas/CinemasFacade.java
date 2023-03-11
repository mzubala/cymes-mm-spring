package pl.com.bottega.cymes.cinemas;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.cinemas.dto.DetailedCinemaHallInfoDto;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class CinemasFacade {

    private final CinemaRepository cinemaRepository;
    private final CinemaHallRepository cinemaHallRepository;
    private final SuspensionRepository suspensionRepository;

    private final CinemaHallsController cinemaHallsController;

    public boolean isCinemaSuspended(Long cinemaId, Instant start, Instant end) {
        if (!cinemaRepository.existsById(cinemaId)) {
            throw new EntityNotFoundException("No such cinema exists");
        }
        return suspensionRepository.existsByCinemaAndFromLessThanEqualAndUntilGreaterThanEqual(
            cinemaRepository.getReferenceById(cinemaId), start, start)
            || suspensionRepository.existsByCinemaAndFromLessThanEqualAndUntilGreaterThanEqual(
            cinemaRepository.getReferenceById(cinemaId), end, end);
    }

    public boolean isCinemaHallSuspended(Long cinemaHallId, Instant start, Instant end) {
        if (!cinemaHallRepository.existsById(cinemaHallId)) {
            throw new EntityNotFoundException("No such cinema hall exists");
        }
        return suspensionRepository.existsByCinemaAndFromLessThanEqualAndUntilGreaterThanEqual(
            cinemaRepository.getReferenceById(cinemaHallId), start, start)
            || suspensionRepository.existsByCinemaAndFromLessThanEqualAndUntilGreaterThanEqual(
            cinemaRepository.getReferenceById(cinemaHallId), end, end);
    }

    public DetailedCinemaHallInfoDto getCinemaHall(Long cinemaHallId) {
        return cinemaHallsController.get(cinemaHallId);
    }
}
