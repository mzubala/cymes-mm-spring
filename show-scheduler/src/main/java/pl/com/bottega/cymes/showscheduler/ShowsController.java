package pl.com.bottega.cymes.showscheduler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.commons.rest.GlobalError;
import pl.com.bottega.cymes.showscheduler.dto.ShowsGroupDto;
import pl.com.bottega.cymes.showscheduler.requests.ScheduleShowRequest;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
class ShowsController {

    private final ShowsService showsService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    void schedule(@Valid @RequestBody ScheduleShowRequest request) {
        showsService.schedule(new ScheduleShowCommand(
            request.cinemaId(), request.cinemaHallId(), request.movieId(), request.when()
        ));
    }

    @GetMapping
    List<ShowsGroupDto> get(@RequestParam Long cinemaId, @RequestParam LocalDate when) {
        return showsService.getShows(cinemaId, when);
    }

    @ExceptionHandler(CinemaHallNotAvailableException.class)
    ResponseEntity<GlobalError> handleCinemaHallNotAvailableException(CinemaHallNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new GlobalError(ex.getMessage()));
    }
}
