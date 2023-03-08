package pl.com.bottega.cymes.reservations;

import java.util.Map;
import java.util.Set;

interface CinemaHallProvider {
    CinemaHall getById(Long cinemaHallId);
}

record CinemaHall(Long cinemaHallId, Map<Integer, Integer> rowNumberToSeatCount) {

    void ensureValidSeats(Set<Seat> seats) {
        // TODO
    }
}
