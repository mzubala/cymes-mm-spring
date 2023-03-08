package pl.com.bottega.cymes.reservations;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.sharedkernel.ClockProvider;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ShowProvider showProvider;
    private final CustomerInformationProvider customerInformationProvider;
    private final CinemaHallProvider cinemaHallProvider;
    private final ClockProvider clockProvider;

    @Transactional
    UUID createReservation(CreateReservationCommand createReservationCommand) {
        return null;
    }
}

record CreateReservationCommand(
    Long showId,
    Long userId,
    Map<TicketKind, Integer> ticketCounts,
    Set<Seat> seats
) {

}