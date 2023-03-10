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
    private final ReceiptCalculator receiptCalculator;

    private final PaymentsFacade paymentsFacade;

    @Transactional
    UUID createReservation(CreateReservationCommand createReservationCommand) {
        Reservation reservation;
        var show = showProvider.getShow(createReservationCommand.showId());
        var cinemaHall = cinemaHallProvider.getById(show.cinemaHallId());
        cinemaHall.ensureValidSeats(createReservationCommand.seats());
        if (createReservationCommand.userId() != null) {
            var customerInformation = customerInformationProvider.getByUserId(createReservationCommand.userId());
            reservation = new Reservation(
                show, customerInformation, createReservationCommand.ticketCounts(), createReservationCommand.seats(),
                receiptCalculator
            );
        }
        else {
            reservation = new Reservation(
                show, createReservationCommand.ticketCounts(), createReservationCommand.seats(), receiptCalculator);
        }
        reservationRepository.save(reservation);
        return reservation.getId();
    }

    @Transactional
    StartedPayment startOnlinePayment(StartPaymentCommand command) {
        return null;
    }

    @Transactional
    void selectOnsitePayment(StartPaymentCommand command) {

    }
}

record CreateReservationCommand(
    Long showId, Long userId, Map<TicketKind, Integer> ticketCounts, Set<Seat> seats
) {

}

record StartPaymentCommand(
    UUID reservationId, AnonymousCustomerInformation anonymousCustomerInformation,
    RegisteredCustomerInformation registeredCustomerInformation
) {

    StartPaymentCommand {
        if((anonymousCustomerInformation == null) == (registeredCustomerInformation == null)) {
           throw new InvalidReservationParamsException("Reservation is either anonymous or non-anonymous");
        }
    }

    record AnonymousCustomerInformation(
        String firstName, String lastName, String email, String phoneNumber
    ) {
    }

    record RegisteredCustomerInformation(
        String phoneNumber
    ) {
    }

}