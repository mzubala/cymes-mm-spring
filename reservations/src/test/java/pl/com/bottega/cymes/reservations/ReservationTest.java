package pl.com.bottega.cymes.reservations;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.com.bottega.cymes.reservations.ReservationBuilder.aReservation;
import static pl.com.bottega.cymes.reservations.ReservationStatus.SEATS_RESERVED;

class ReservationTest {

    @Test
    void newReservationHasSeatsSelectedStatus() {
        // given
        var showId = 1L;
        var ticketCounts = Map.of(TicketKind.REGULAR, 2);
        var seats = Set.of(new Seat(10, 10), new Seat(10, 11));

        // when
        var reservation = new Reservation(showId, ticketCounts, seats);

        // then
        assertThat(reservation.getStatus()).isEqualTo(SEATS_RESERVED);
    }

    @Test
    void cannotCreateReservationWithInvalidParams() {
        // expect
        assertThatThrownBy(() -> aReservation().withShowId(null).build()).isInstanceOf(InvalidReservationParamsException.class);
        assertThatThrownBy(() -> aReservation().withSeats(null).build()).isInstanceOf(InvalidReservationParamsException.class);
        assertThatThrownBy(() -> aReservation().withTicketCounts(null).build()).isInstanceOf(InvalidReservationParamsException.class);
        assertThatThrownBy(() -> aReservation().withTicketCounts(Map.of()).withSeats(Set.of()).build())
            .isInstanceOf(InvalidReservationParamsException.class);
        assertThatThrownBy(() -> aReservation().withTicketCounts(Map.of(TicketKind.REGULAR, 10))
            .withSeats(Set.of(new Seat(1, 1))).build())
            .isInstanceOf(InvalidReservationParamsException.class);
    }

}
