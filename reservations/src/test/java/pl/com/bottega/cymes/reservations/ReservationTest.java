package pl.com.bottega.cymes.reservations;

import org.junit.jupiter.api.Test;
import pl.com.bottega.cymes.reservations.dto.AnonymousCustomerInformation;
import pl.com.bottega.cymes.reservations.dto.RegisteredCustomerInformation;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.com.bottega.cymes.reservations.CustomerInformationBuilder.aCustomerInformation;
import static pl.com.bottega.cymes.reservations.ReservationBuilder.aReservation;
import static pl.com.bottega.cymes.reservations.ReservationStatus.SEATS_RESERVED;

class ReservationTest {

    @Test
    void newReservationHasSeatsSelectedStatus() {
        // expect
        assertThat(aReservation().build().getStatus()).isEqualTo(SEATS_RESERVED);
    }

    @Test
    void cannotCreateReservationWithInvalidParams() {
        // expect
        assertThatThrownBy(() -> aReservation().withShowDto(null).build()).isInstanceOf(
            InvalidReservationParamsException.class);
        assertThatThrownBy(() -> aReservation().withSeats(null).build()).isInstanceOf(
            InvalidReservationParamsException.class);
        assertThatThrownBy(() -> aReservation().withTicketCounts(null).build()).isInstanceOf(
            InvalidReservationParamsException.class);
        assertThatThrownBy(() -> aReservation().withTicketCounts(Map.of()).withSeats(Set.of()).build()).isInstanceOf(
            InvalidReservationParamsException.class);
        assertThatThrownBy(() -> aReservation().withTicketCounts(Map.of(TicketKind.REGULAR, 10))
            .withSeats(Set.of(new Seat(1, 1))).build()).isInstanceOf(InvalidReservationParamsException.class);
        assertThatThrownBy(
            () -> aReservation().withCustomerInformation(aCustomerInformation().withUserId(null)).build()).isInstanceOf(
            InvalidReservationParamsException.class);
        assertThatThrownBy(() -> aReservation().withCustomerInformation(aCustomerInformation().withFirstName(null))
            .build()).isInstanceOf(InvalidReservationParamsException.class);
        assertThatThrownBy(() -> aReservation().withCustomerInformation(aCustomerInformation().withLastName(null))
            .build()).isInstanceOf(InvalidReservationParamsException.class);
        assertThatThrownBy(() -> aReservation().withTicketCounts(Map.of(TicketKind.REGULAR, 0)).build()).isInstanceOf(
            InvalidReservationParamsException.class);
        assertThatThrownBy(() -> aReservation().withTicketCounts(Map.of(TicketKind.REGULAR, -1)).build()).isInstanceOf(
            InvalidReservationParamsException.class);
    }

    @Test
    void calculatesReceiptUponCreation() {
        // given
        var reservation = aReservation().withTicketCounts(Map.of(TicketKind.STUDENT, 1, TicketKind.REGULAR, 2))
            .withSeats(Set.of(new Seat(1, 1), new Seat(1, 2), new Seat(1, 3))).build();

        // expect
        assertThat(reservation.getReceipt().getTotal()).isEqualTo(new Money(30));
    }

    @Test
    void cannotStartPaymentIfOnlinePaymentHasBeenStarted() {
        // given
        var reservation = aReservation().nonAnonymous().build();
        reservation.startOnlinePayment("test", null, aRegisteredCustomerInformation());

        // expect
        assertThatThrownBy(() -> {
            reservation.startOnlinePayment("test", null, aRegisteredCustomerInformation());
        }).isInstanceOf(IllegalReservationOperationException.class);
        assertThatThrownBy(() -> {
            reservation.startOnsitePayment(null, aRegisteredCustomerInformation());
        }).isInstanceOf(IllegalReservationOperationException.class);
    }

    @Test
    void cannotStartPaymentIfOnsitePaymentHasBeenStarted() {
        // given
        var reservation = aReservation().nonAnonymous().build();
        reservation.startOnsitePayment(null, aRegisteredCustomerInformation());

        // expect
        assertThatThrownBy(() -> {
            reservation.startOnlinePayment("test", null, aRegisteredCustomerInformation());
        }).isInstanceOf(IllegalReservationOperationException.class);
        assertThatThrownBy(() -> {
            reservation.startOnsitePayment(null, aRegisteredCustomerInformation());
        }).isInstanceOf(IllegalReservationOperationException.class);
    }

    @Test
    void anonymousReservationRequiresFullCustomerInformationToStartPayment() {
        // given
        var anonymousReservation = aReservation().anonymous().build();

        // expect
        assertThatThrownBy(() -> {
            anonymousReservation.startOnsitePayment(null, null);
        }).isInstanceOf(InvalidReservationParamsException.class);
        assertThatThrownBy(() -> {
            anonymousReservation.startOnlinePayment("test", null, null);
        }).isInstanceOf(InvalidReservationParamsException.class);
        assertThatThrownBy(() -> {
            anonymousReservation.startOnsitePayment(null, aRegisteredCustomerInformation());
        }).isInstanceOf(InvalidReservationParamsException.class);
        assertThatThrownBy(() -> {
            anonymousReservation.startOnlinePayment("test", null, aRegisteredCustomerInformation());
        }).isInstanceOf(InvalidReservationParamsException.class);
    }

    @Test
    void nonAnonymousReservationRequiresFullCustomerInformationToStartPayment() {
        // given
        var nonAnonymousReservation = aReservation().nonAnonymous().build();

        // expect
        assertThatThrownBy(() -> {
            nonAnonymousReservation.startOnsitePayment(null, null);
        }).isInstanceOf(InvalidReservationParamsException.class);
        assertThatThrownBy(() -> {
            nonAnonymousReservation.startOnlinePayment("test", null, null);
        }).isInstanceOf(InvalidReservationParamsException.class);
        assertThatThrownBy(() -> {
            nonAnonymousReservation.startOnsitePayment(anAnonymousCustomerInformation(), null);
        }).isInstanceOf(InvalidReservationParamsException.class);
        assertThatThrownBy(() -> {
            nonAnonymousReservation.startOnlinePayment("test", anAnonymousCustomerInformation(), null);
        }).isInstanceOf(InvalidReservationParamsException.class);
    }

    private AnonymousCustomerInformation anAnonymousCustomerInformation() {
        return new AnonymousCustomerInformation("X", "Y", "x@test.com", "400 400 400");
    }

    private static RegisteredCustomerInformation aRegisteredCustomerInformation() {
        return new RegisteredCustomerInformation("500 500 500");
    }
}
