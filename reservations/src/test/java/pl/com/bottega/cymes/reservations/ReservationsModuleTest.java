package pl.com.bottega.cymes.reservations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.com.bottega.cymes.commons.test.FixedClockProvider;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class ReservationsModuleTest {

    private final InMemoryReservationRepository reservationRepository = new InMemoryReservationRepository();

    private final InMemoryShowProvider showProvider = new InMemoryShowProvider();

    private final InMemoryCustomerInformationProvider customerInformationProvider = new InMemoryCustomerInformationProvider();

    private final FixedClockProvider clockProvider = new FixedClockProvider();

    private final ReservationService reservationService = new ReservationService(reservationRepository, showProvider, customerInformationProvider, clockProvider);

    private Long showId = 1L;
    private Long userId = 1L;

    private Map<TicketKind, Integer> ticketCounts = Map.of(TicketKind.REGULAR, 2);
    private Set<Seat> seats = Set.of(new Seat(10, 10), new Seat(10, 11));

    @BeforeEach
    void setup() {

    }

    @Test
    void createsANewAnonymousReservation() {
        // when
        var reservationId = reservationService.createReservation(new CreateReservationCommand(
            showId,
            null,
            ticketCounts,
            seats
        ));

        // then
        var savedReservation = reservationRepository.getReferenceById(reservationId);
        assertThat(savedReservation.getId()).isEqualTo(reservationId);
        assertThat(savedReservation.getShowId()).isEqualTo(showId);
        assertThat(savedReservation.getUserId()).isNull();
        assertThat(savedReservation.getTicketCounts()).isEqualTo(Map.of(TicketKind.REGULAR, 2));
        assertThat(savedReservation.getSeats()).containsExactly(new Seat(10, 10), new Seat(10, 11));
    }

    @Test
    void createsANewNonAnonymousReservation() {
        // when
        var reservationId = reservationService.createReservation(new CreateReservationCommand(
            showId,
            userId,
            ticketCounts,
            seats
        ));

        // then
        var savedReservation = reservationRepository.getReferenceById(reservationId);
        assertThat(savedReservation.getUserId()).isEqualTo(userId);
    }
}
