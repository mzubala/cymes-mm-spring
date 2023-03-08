package pl.com.bottega.cymes.reservations;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import pl.com.bottega.cymes.showscheduler.dto.ShowDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
class Reservation {
    @Id
    private UUID id;

    protected Reservation() {}

    Reservation(ShowDto showDto, Map<TicketKind, Integer> ticketCounts, Set<Seat> seats) {

    }

    public Reservation(ShowDto showDto, CustomerInformation customerInformation, Map<TicketKind, Integer> ticketCounts, Set<Seat> seats) {

    }

    Long getShowId() {
        return null;
    }

    Long getUserId() {
        return null;
    }

    Map<TicketKind, Integer> getTicketCounts() {
        return Map.of();
    }

    Set<Seat> getSeats() {
        return Set.of();
    }

    UUID getId() {
        return id;
    }

    ReservationStatus getStatus() {
        return null;
    }
}

enum ReservationStatus {
    DRAFT, SEATS_RESERVED,
    WAITING_ONLINE_PAYMENT,
    WAITING_ONSITE_PAYMENT,
    PAID,
    CANCELED
}

class InvalidReservationParamsException extends RuntimeException {
    InvalidReservationParamsException(String message) {
        super(message);
    }
}