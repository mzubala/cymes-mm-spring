package pl.com.bottega.cymes.showscheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.cinemas.CinemasFacade;
import pl.com.bottega.cymes.movies.MoviesFacade;
import pl.com.bottega.cymes.movies.MoviesFacade.MovieDto;
import pl.com.bottega.cymes.showscheduler.dto.ShowsGroupDto;
import pl.com.bottega.cymes.showscheduler.dto.ShowsGroupDto.ShowDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
class ShowsService {

    private final ShowRepository showRepository;
    private final CinemasFacade cinemasFacade;
    private final MoviesFacade moviesFacade;

    @Transactional
    void schedule(ScheduleShowCommand command) {
        var movie = moviesFacade.getMovie(command.movieId());
        var show = new Show(
            command.movieId(),
            command.cinemaId(),
            command.cinemaHallId(),
            command.when(),
            command.when().plus(movie.durationMinutes(), ChronoUnit.MINUTES)
        );
        if (cinemasFacade.isCinemaSuspended(command.cinemaId(), show.getFrom(), show.getUntil())) {
            throw new CinemaHallNotAvailableException("Cinema is suspended at the selected time");
        }
        if (cinemasFacade.isCinemaHallSuspended(command.cinemaHallId(), show.getFrom(), show.getUntil())) {
            throw new CinemaHallNotAvailableException("Cinema hall is suspended at the selected time");
        }
        if (showRepository.collidingShowsPresent(show.getFrom(), show.getUntil(), command.cinemaId(), command.cinemaHallId())) {
            throw new CinemaHallNotAvailableException("Another show has been scheduled in this cinema and cinema hall at the colliding time");
        }
        showRepository.save(show);
    }

    @Transactional(readOnly = true)
    List<ShowsGroupDto> getShows(Long cinemaId, LocalDate at) {
        var startOfDay = at.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        var endOfDay = startOfDay.plus(1, ChronoUnit.DAYS);
        var showMap = showRepository.findByCinemaIdAndFromAfterAndUntilBefore(cinemaId, startOfDay, endOfDay)
            .collect(Collectors.groupingBy(Show::getMovieId));
        var moviesMap = moviesFacade.getMovies(showMap.keySet()).stream()
            .collect(Collectors.toMap(MovieDto::id, Function.identity()));
        return showMap.entrySet().stream().map(entry -> {
            var movie = moviesMap.get(entry.getKey());
            return new ShowsGroupDto(
                toMovieDto(movie),
                toShowDtos(entry.getValue())
            );
        }).collect(toList());
    }

    private static List<ShowDto> toShowDtos(List<Show> shows) {
        return shows.stream().map(ShowsService::toShowDto).collect(toList());
    }

    private static ShowsGroupDto.MovieDto toMovieDto(MovieDto movie) {
        return new ShowsGroupDto.MovieDto(movie.id(), movie.title());
    }

    private static ShowDto toShowDto(Show show) {
        return new ShowDto(show.getId(), show.getStartTime());
    }
}

record ScheduleShowCommand(
    Long cinemaId,
    Long cinemaHallId,
    Long movieId,
    Instant when
) {
}
