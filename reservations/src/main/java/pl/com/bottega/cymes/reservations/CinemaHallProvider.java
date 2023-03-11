package pl.com.bottega.cymes.reservations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.cinemas.CinemasFacade;
import pl.com.bottega.cymes.cinemas.RowElementKind;
import pl.com.bottega.cymes.cinemas.dto.RowDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

interface CinemaHallProvider {
    CinemaHall getById(Long cinemaHallId);
}

record CinemaHall(Long cinemaHallId, Map<Integer, Integer> rowNumberToSeatCount) {

    void ensureValidSeats(Set<Seat> seats) {
        seats.forEach(this::ensureValidSeat);
    }

    private void ensureValidSeat(Seat seat) {
        var numberOfSeatsInRow = rowNumberToSeatCount.get(seat.rowNumber());
        if (numberOfSeatsInRow == null || numberOfSeatsInRow < seat.seatNumber()) {
            throw new InvalidReservationParamsException("Seat does not exist in the cinema hall");
        }
    }
}

@Component
@RequiredArgsConstructor
class CinemasModuleCinemaHallProvider implements CinemaHallProvider {

    private final CinemasFacade cinemasFacade;

    @Override
    public CinemaHall getById(Long cinemaHallId) {
        var cinemaHall = cinemasFacade.getCinemaHall(cinemaHallId);
        Map<Integer, Integer> rowNumberToSeatCount = new HashMap<>();
        cinemaHall.rows().forEach(row -> rowNumberToSeatCount.put(row.number(), seatsCount(row)));
        return new CinemaHall(cinemaHallId, rowNumberToSeatCount);
    }

    private static int seatsCount(RowDto row) {
        return (int) row.elements().stream().filter(e -> e.kind() == RowElementKind.SEAT).count();
    }
}