package pl.com.bottega.cymes.cinemas;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.cinemas.dto.BasicCinemaHallInfoDto;
import pl.com.bottega.cymes.cinemas.dto.BasicCinemaInfoDto;
import pl.com.bottega.cymes.cinemas.dto.DetailedCinemaInfoDto;
import pl.com.bottega.cymes.cinemas.dto.SuspensionCheckDto;
import pl.com.bottega.cymes.cinemas.dto.SuspensionDto;
import pl.com.bottega.cymes.cinemas.requests.CreateCinemaRequest;
import pl.com.bottega.cymes.cinemas.requests.SuspendRequest;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestMapping("/cinemas")
@RestController
@RequiredArgsConstructor
public class CinemasController {

    private final CinemaRepository cinemaRepository;
    private final SuspensionRepository suspensionRepository;

    private final CinemaHallRepository cinemaHallRepository;

    @PostMapping
    public void create(@Valid @RequestBody CreateCinemaRequest request) {
        cinemaRepository.save(new Cinema(request.name(), request.city()));
    }

    @GetMapping
    public List<BasicCinemaInfoDto> getAll() {
        return cinemaRepository.findByOrderByNameAscCityAsc(BasicCinemaInfoDto.class);
    }

    @PostMapping("/{id}/suspensions")
    public void suspend(@PathVariable("id") Long cinemaId, @Valid @RequestBody SuspendRequest suspendRequest) {
        suspensionRepository.save(new Suspension(cinemaRepository.getReferenceById(cinemaId), suspendRequest.from(), suspendRequest.until()));
    }

    @DeleteMapping("/suspensions/{id}")
    @Transactional
    public void cancelSuspension(@PathVariable("id") Long suspensionId) {
        var suspension = suspensionRepository.getReferenceById(suspensionId);
        suspension.cancel();
    }

    @GetMapping("/{cinemaId}/suspensions")
    public List<SuspensionDto> getSuspensions(@PathVariable("cinemaId") Long cinemaId) {
        return suspensionRepository.findByCinemaOrderByFromAscUntilAsc(SuspensionDto.class, cinemaRepository.getReferenceById(cinemaId));
    }

    @GetMapping("/{cinemaId}/suspensions/check")
    public SuspensionCheckDto suspensionCheck(@PathVariable("cinemaId") Long cinemaId, @RequestParam("at") Instant at) {
        return new SuspensionCheckDto(suspensionRepository.existsByCinemaAndFromLessThanEqualAndUntilGreaterThanEqual(
            cinemaRepository.getReferenceById(cinemaId),
            at, at
        ));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public DetailedCinemaInfoDto get(@PathVariable("id") Long cinemaId, @RequestParam("at") Instant at) {
        var cinema = cinemaRepository.getReferenceById(cinemaId);
        var halls = cinemaHallRepository.findByCinema(cinema);
        var suspensions = suspensionRepository.findByCinemaHallInAndFromLessThanEqualAndUntilGreaterThanEqual(halls, at, at);
        return new DetailedCinemaInfoDto(
            cinema.getId(),
            cinema.getName(),
            cinema.getCity(),
            toBasicCinemaHallInfoDto(halls, suspensions),
            suspensionRepository.existsByCinemaAndFromLessThanEqualAndUntilGreaterThanEqual(cinema, at, at)
        );
    }

    private static List<BasicCinemaHallInfoDto> toBasicCinemaHallInfoDto(List<CinemaHall> halls, List<Suspension> suspensions) {
        return halls.stream().map(hall -> toBasicCinemaHallInfoDto(suspensions, hall)).collect(toList());
    }

    private static BasicCinemaHallInfoDto toBasicCinemaHallInfoDto(List<Suspension> suspensions, CinemaHall hall) {
        return new BasicCinemaHallInfoDto(hall.getId(), hall.getName(), hall.getCapacity(), isSuspended(suspensions, hall));
    }

    private static boolean isSuspended(List<Suspension> suspensions, CinemaHall hall) {
        return suspensions.stream().anyMatch(s -> s.getCinemaHall().getId().equals(hall.getId()));
    }
}

