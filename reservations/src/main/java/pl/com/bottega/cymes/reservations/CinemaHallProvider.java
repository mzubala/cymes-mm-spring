package pl.com.bottega.cymes.reservations;

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
