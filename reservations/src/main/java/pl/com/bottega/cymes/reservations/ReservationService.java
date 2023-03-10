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

    StartedPayment startOnlinePayment(StartPaymentCommand command) {
        var reservation = reservationRepository.getReferenceById(command.reservationId());
        var startedPayment = paymentsFacade.startPayment(reservation.getId(), reservation.getReceipt().getTotal());
        reservation.startOnlinePayment(startedPayment.id(), command.anonymousCustomerInformation(), command.registeredCustomerInformation());
        reservationRepository.save(reservation);
        return startedPayment;
    }

    @Transactional
    void startOnsitePayment(StartPaymentCommand command) {
        var reservation = reservationRepository.getReferenceById(command.reservationId());
        reservation.startOnsitePayment(command.anonymousCustomerInformation(), command.registeredCustomerInformation());
        reservationRepository.save(reservation);
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