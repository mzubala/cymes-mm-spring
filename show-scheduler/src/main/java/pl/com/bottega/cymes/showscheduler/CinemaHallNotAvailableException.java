package pl.com.bottega.cymes.showscheduler;

class CinemaHallNotAvailableException extends RuntimeException {
    CinemaHallNotAvailableException(String message) {
        super(message);
    }
}
