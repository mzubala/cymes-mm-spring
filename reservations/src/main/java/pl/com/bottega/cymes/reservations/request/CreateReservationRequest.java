package pl.com.bottega.cymes.reservations.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import pl.com.bottega.cymes.reservations.Seat;
import pl.com.bottega.cymes.reservations.TicketKind;

import java.util.List;
import java.util.Map;

public record CreateReservationRequest(
    @NotNull Long showId, @NotEmpty Map<TicketKind, Integer> ticketCounts, @NotEmpty List<Seat> seats
) {
}
