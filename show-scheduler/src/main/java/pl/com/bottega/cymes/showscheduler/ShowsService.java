package pl.com.bottega.cymes.showscheduler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.cinemas.CinemasFacade;
import pl.com.bottega.cymes.commons.application.Audited;
import pl.com.bottega.cymes.commons.application.InjectUserId;
import pl.com.bottega.cymes.movies.MoviesFacade;
import pl.com.bottega.cymes.movies.MoviesFacade.MovieDto;
import pl.com.bottega.cymes.sharedkernel.UserCommand;
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
    private final ShowSchedulerProperties showSchedulerProperties;

    @Transactional
    @Audited
    void schedule(@InjectUserId ScheduleShowCommand command) {
        var movie = moviesFacade.getMovie(command.getMovieId());
        var show = createNewShow(command, movie);
        if (cinemasFacade.isCinemaSuspended(command.getCinemaId(), show.getFrom(), show.getUntil())) {
            throw new CinemaHallNotAvailableException("Cinema is suspended at the selected time");
        }
        if (cinemasFacade.isCinemaHallSuspended(command.getCinemaHallId(), show.getFrom(), show.getUntil())) {
            throw new CinemaHallNotAvailableException("Cinema hall is suspended at the selected time");
        }
        if (showRepository.collidingShowsPresent(
            show.getFrom(), show.getUntil(), command.getCinemaId(), command.getCinemaHallId())) {
            throw new CinemaHallNotAvailableException(
                "Another show has been scheduled in this cinema and cinema hall at the colliding time");
        }
        showRepository.save(show);
    }

    private Show createNewShow(ScheduleShowCommand command, MovieDto movie) {
        return new Show(command.getMovieId(), command.getCinemaId(), command.getCinemaHallId(), command.getWhen(),
            showEndTime(command, movie)
        );
    }

    private Instant showEndTime(ScheduleShowCommand command, MovieDto movie) {
        return command.getWhen().plus(movie.durationMinutes(), ChronoUnit.MINUTES).plus(
            showSchedulerProperties.cinemaHallCleaningTime()).plus(showSchedulerProperties.commercialsDisplayTime());
    }

    @Transactional(readOnly = true)
    List<ShowsGroupDto> getShows(Long cinemaId, LocalDate at) {
        var startOfDay = at.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        var endOfDay = startOfDay.plus(1, ChronoUnit.DAYS);
        var showMap = showRepository.findByCinemaIdAndFromAfterAndUntilBefore(cinemaId, startOfDay, endOfDay).collect(
            Collectors.groupingBy(Show::getMovieId));
        var moviesMap = moviesFacade.getMovies(showMap.keySet()).stream().collect(
            Collectors.toMap(MovieDto::id, Function.identity()));
        return showMap.entrySet().stream().map(entry -> {
            var movie = moviesMap.get(entry.getKey());
            return new ShowsGroupDto(toMovieDto(movie), toShowDtos(entry.getValue()));
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

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
class ScheduleShowCommand extends UserCommand {
    Long cinemaId;
    Long cinemaHallId;
    Long movieId;
    Instant when;
}
