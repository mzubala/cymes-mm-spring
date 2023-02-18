package pl.com.bottega.cymes.showscheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.cinemas.CinemasFacade;
import pl.com.bottega.cymes.movies.MoviesFacade;
import pl.com.bottega.cymes.showscheduler.dto.ShowsGroupDto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
class ShowsService {

    private final ShowRepository showRepository;
    private final CinemasFacade cinemasFacade;
    private final MoviesFacade moviesFacade;

    void schedule(ScheduleShowCommand command) {
        // TODO
    }

    List<ShowsGroupDto> getShows(Long cinemaId, LocalDate at) {
        // TODO
        return List.of();
    }
}

record ScheduleShowCommand(
    Long cinemaId,
    Long cinemaHallId,
    Long movieId,
    Instant when
) {}
