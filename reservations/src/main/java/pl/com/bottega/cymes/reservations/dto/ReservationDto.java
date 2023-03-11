package pl.com.bottega.cymes.reservations.dto;

import pl.com.bottega.cymes.reservations.CustomerInformation;
import pl.com.bottega.cymes.reservations.ReservationStatus;
import pl.com.bottega.cymes.reservations.Seat;
import pl.com.bottega.cymes.reservations.TicketKind;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record ReservationDto(
    UUID reservationId,
    Long showId,
    Long cinemaId,
    Long cinemaHallId,
    ReservationStatus status,
    Map<TicketKind, Integer> ticketCount,
    Set<Seat> seats,
    ReceiptDto receipt,
    CustomerInformation customerInformation
) {
}
