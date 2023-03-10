package pl.com.bottega.cymes.cinemas;

import com.google.common.collect.Streams;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.cinemas.dto.DetailedCinemaHallInfoDto;
import pl.com.bottega.cymes.cinemas.dto.RowDto;
import pl.com.bottega.cymes.cinemas.dto.RowElementDto;
import pl.com.bottega.cymes.cinemas.dto.SuspensionCheckDto;
import pl.com.bottega.cymes.cinemas.dto.SuspensionDto;
import pl.com.bottega.cymes.cinemas.requests.CreateCinemaHallRequest;
import pl.com.bottega.cymes.cinemas.requests.SuspendRequest;
import pl.com.bottega.cymes.cinemas.requests.UpdateCinemaHallRequest;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/halls")
@RequiredArgsConstructor
class CinemaHallsController {

    private final CinemaHallRepository cinemaHallRepository;
    private final CinemaRepository cinemaRepository;

    private final SuspensionRepository suspensionRepository;

    @PostMapping
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    void create(@Valid @RequestBody CreateCinemaHallRequest request) {
        cinemaHallRepository.save(new CinemaHall(cinemaRepository.getReferenceById(request.cinemaId()), request.name(),
            toRows(request.layout())
        ));
    }

    @GetMapping("/{hallId}")
    DetailedCinemaHallInfoDto get(@PathVariable("hallId") Long cinemaHallId) {
        var cinemaHall = cinemaHallRepository.getReferenceById(cinemaHallId);
        Instant now = Instant.now();
        return new DetailedCinemaHallInfoDto(cinemaHallId, cinemaHall.getName(), cinemaHall.getCapacity(),
            toRowDtos(cinemaHall),
            suspensionRepository.existsByCinemaHallAndFromLessThanEqualAndUntilGreaterThanEqual(cinemaHall, now, now)
        );
    }

    @PostMapping("/{hallId}/suspensions")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    void suspend(@PathVariable("hallId") Long hallId, @Valid @RequestBody SuspendRequest suspendRequest) {
        suspensionRepository.save(new Suspension(cinemaHallRepository.getReferenceById(hallId), suspendRequest.from(),
            suspendRequest.until()
        ));
    }

    @PutMapping("/{hallId}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    void update(
        @PathVariable("hallId") Long hallId, @Valid @RequestBody UpdateCinemaHallRequest updateCinemaHallRequest
    ) {
        var hall = cinemaHallRepository.getReferenceById(hallId);
        hall.setRows(toRows(updateCinemaHallRequest.layout()));
    }

    @GetMapping("/{hallId}/suspensions")
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    List<SuspensionDto> getSuspensions(@PathVariable("hallId") Long hallId) {
        return suspensionRepository.findByCinemaHallOrderByFromAscUntilAsc(
            SuspensionDto.class, cinemaHallRepository.getReferenceById(hallId));
    }

    @GetMapping("/{hallId}/suspensions/check")
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    SuspensionCheckDto suspensionCheck(@PathVariable("hallId") Long hallId, @RequestParam("at") Instant at) {
        return new SuspensionCheckDto(
            suspensionRepository.existsByCinemaHallAndFromLessThanEqualAndUntilGreaterThanEqual(
                cinemaHallRepository.getReferenceById(hallId), at, at));
    }

    private static List<RowElement> toRowElements(RowDto rowDto) {
        return rowDto.elements().stream().map(
            rowElementDto -> new RowElement(rowElementDto.number(), rowElementDto.kind())).collect(toList());
    }

    private static List<RowElementDto> toRowElementDtos(Row row) {
        return Streams.mapWithIndex(row.getElements().stream(),
            (element, index) -> new RowElementDto((int) index, element.getNumber(), element.getElementKind())
        ).collect(toList());
    }

    static List<RowDto> toRowDtos(CinemaHall cinemaHall) {
        return cinemaHall.getRows().stream().map(row -> new RowDto(row.getNumber(), toRowElementDtos(row))).collect(
            toList());
    }

    private static List<Row> toRows(List<RowDto> layout) {
        return layout.stream().map(rowDto -> new Row(rowDto.number(), toRowElements(rowDto))).collect(toList());
    }
}