package pl.com.bottega.cymes.cinemas;

import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.cinemas.dto.DetailedCinemaHallInfoDto;
import pl.com.bottega.cymes.cinemas.dto.RowDto;
import pl.com.bottega.cymes.cinemas.dto.RowElementDto;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
class CinemaHallsService {

    private final CinemaHallRepository cinemaHallRepository;

    private final SuspensionRepository suspensionRepository;

    DetailedCinemaHallInfoDto getDetailedCinemaHallInfoDto(
        Long cinemaHallId, CinemaHallsController cinemaHallsController
    ) {
        var cinemaHall = cinemaHallRepository.getReferenceById(cinemaHallId);
        Instant now = Instant.now();
        return new DetailedCinemaHallInfoDto(cinemaHallId, cinemaHall.getName(), cinemaHall.getCapacity(),
            toRowDtos(cinemaHall),
            suspensionRepository.existsByCinemaHallAndFromLessThanEqualAndUntilGreaterThanEqual(cinemaHall, now, now)
        );
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

    static List<Row> toRows(List<RowDto> layout) {
        return layout.stream().map(rowDto -> new Row(rowDto.number(), toRowElements(rowDto))).collect(toList());
    }
}
