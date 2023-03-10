package pl.com.bottega.cymes.reservations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.com.bottega.cymes.commons.test.FixedClockProvider;
import pl.com.bottega.cymes.reservations.StartPaymentCommand.AnonymousCustomerInformation;
import pl.com.bottega.cymes.showscheduler.dto.ShowDto;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.com.bottega.cymes.reservations.CinemaHallBuilder.aCinemaHall;
import static pl.com.bottega.cymes.reservations.CustomerInformationBuilder.aCustomerInformation;
import static pl.com.bottega.cymes.reservations.RecipeCalculatorFixtures.defaultReceiptCalculator;
import static pl.com.bottega.cymes.reservations.ReservationStatus.WAITING_ONSITE_PAYMENT;
import static pl.com.bottega.cymes.reservations.ShowDtoBuilder.aShowDto;


class ReservationsModuleTest {

    private final InMemoryReservationRepository reservationRepository = new InMemoryReservationRepository();

    private final InMemoryShowProvider showProvider = new InMemoryShowProvider();

    private final InMemoryCustomerInformationProvider customerInformationProvider
        = new InMemoryCustomerInformationProvider();

    private final FixedClockProvider clockProvider = new FixedClockProvider();

    private final InMemoryCinemaHallProvider cinemaHallProvider = new InMemoryCinemaHallProvider();

    private final FakePaymentFacade fakePaymentFacade = new FakePaymentFacade();

    private final ReservationService reservationService = new ReservationService(reservationRepository, showProvider,
        customerInformationProvider, cinemaHallProvider, clockProvider, defaultReceiptCalculator(), fakePaymentFacade
    );

    private ShowDto show = aShowDto().build();
    private CustomerInformation customerInformation = aCustomerInformation().build();
    private CinemaHall cinemaHall = aCinemaHall().withCinemaHallId(show.cinemaHallId()).withRow(10, 30).build();

    private Map<TicketKind, Integer> ticketCounts = Map.of(TicketKind.REGULAR, 2);
    private Set<Seat> seats = Set.of(new Seat(10, 10), new Seat(10, 11));

    @BeforeEach
    void setup() {
        showProvider.save(show.showId(), show);
        customerInformationProvider.save(customerInformation.userId(), customerInformation);
        cinemaHallProvider.save(cinemaHall.cinemaHallId(), cinemaHall);
    }

    @Test
    void createsANewAnonymousReservation() {
        // when
        var reservationId = reservationService.createReservation(
            new CreateReservationCommand(show.showId(), null, ticketCounts, seats));

        // then
        var savedReservation = reservationRepository.getReferenceById(reservationId);
        assertThat(savedReservation.getId()).isEqualTo(reservationId);
        assertThat(savedReservation.getShowId()).isEqualTo(show.showId());
        assertThat(savedReservation.getUserId()).isNull();
        assertThat(savedReservation.getTicketCounts()).isEqualTo(Map.of(TicketKind.REGULAR, 2));
        assertThat(savedReservation.getSeats()).containsExactly(new Seat(10, 10), new Seat(10, 11));
    }

    @Test
    void createsANewNonAnonymousReservation() {
        // when
        var reservationId = reservationService.createReservation(
            new CreateReservationCommand(show.showId(), customerInformation.userId(), ticketCounts, seats));

        // then
        var savedReservation = reservationRepository.getReferenceById(reservationId);
        assertThat(savedReservation.getUserId()).isEqualTo(customerInformation.userId());
    }

    @Test
    void cannotCreateReservationWithSeatsNonExistingInTheCinemaHall() {
        // expect
        assertThatThrownBy(() -> {
            reservationService.createReservation(new CreateReservationCommand(show.showId(), null, ticketCounts,
                Set.of(new Seat(10, 10), new Seat(999, 11))
            ));
        }).isInstanceOf(InvalidReservationParamsException.class);
    }

    @Test
    void selectsOnsitePaymentForAnonymousReservation() {
        // given
        var reservationId = reservationService.createReservation(
            new CreateReservationCommand(show.showId(), null, ticketCounts, seats));

        // when
        reservationService.selectOnsitePayment(new StartPaymentCommand(reservationId,
            new AnonymousCustomerInformation("John", "Doe", "john@test.com", "600 000 000"), null
        ));

        // then
        var savedReservation = reservationRepository.getReferenceById(reservationId);
        assertThat(savedReservation.getStatus()).isEqualTo(WAITING_ONSITE_PAYMENT);
        assertThat(savedReservation.getPayment().getMethod()).isEqualTo(PaymentMethod.ONSITE);
        assertThat(savedReservation.getCustomerInfromation()).isEqualTo(
            new CustomerInformation(null, "John", "Doe", "john@test.com", "600 000 000"));
    }

    @Test
    void startsOnlinePaymentForAnonymousReservation() {
        // given
        var reservationId = reservationService.createReservation(
            new CreateReservationCommand(show.showId(), null, ticketCounts, seats));

        // when
        var startedPayment = reservationService.startOnlinePayment(new StartPaymentCommand(reservationId,
            new AnonymousCustomerInformation("John", "Doe", "john@test.com", "600 000 000"), null
        ));

        // then
        var savedReservation = reservationRepository.getReferenceById(reservationId);
        assertThat(savedReservation.getStatus()).isEqualTo(WAITING_ONSITE_PAYMENT);
        assertThat(savedReservation.getPayment().getMethod()).isEqualTo(PaymentMethod.ONLINE);
        assertThat(savedReservation.getPayment().getExternalId()).isEqualTo(fakePaymentFacade.getLastStartedPayment().id());
        assertThat(savedReservation.getCustomerInfromation()).isEqualTo(
            new CustomerInformation(null, "John", "Doe", "john@test.com", "600 000 000"));
        assertThat(startedPayment).isEqualTo(fakePaymentFacade.getLastStartedPayment());
        assertThat(fakePaymentFacade.getLastStartedPaymentAmount()).isEqualTo(new Money(20));
    }
}
