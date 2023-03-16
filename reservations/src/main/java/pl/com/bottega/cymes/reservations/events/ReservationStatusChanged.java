package pl.com.bottega.cymes.reservations.events;

import pl.com.bottega.cymes.reservations.ReservationStatus;
import pl.com.bottega.cymes.sharedkernel.Event;

import java.util.UUID;

public record ReservationStatusChanged(
    UUID reservationId,
    ReservationStatus oldStatus,
    ReservationStatus newStatus
) implements Event {
}
