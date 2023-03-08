package pl.com.bottega.cymes.reservations;

import java.util.Map;

interface CinemaHallProvider {
    CinemaHall getById(Long cinemaHallId);
}

record CinemaHall(Long cinemaHallId, Map<Integer, Integer> rowNumberToSeatCount) {

}
