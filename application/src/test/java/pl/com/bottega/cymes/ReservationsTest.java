package pl.com.bottega.cymes;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.commons.test.IntegrationTest;
import pl.com.bottega.cymes.reservations.CustomerInformation;
import pl.com.bottega.cymes.reservations.ReservationStatus;
import pl.com.bottega.cymes.reservations.Seat;
import pl.com.bottega.cymes.reservations.TicketKind;
import pl.com.bottega.cymes.reservations.dto.ReceiptDto;
import pl.com.bottega.cymes.reservations.dto.ReceiptDto.ReceiptLineDto;
import pl.com.bottega.cymes.reservations.dto.RegisteredCustomerInformation;
import pl.com.bottega.cymes.reservations.dto.ReservationDto;
import pl.com.bottega.cymes.reservations.request.CreateReservationRequest;
import pl.com.bottega.cymes.reservations.request.CreateReservationResponse;
import pl.com.bottega.cymes.reservations.request.StartPaymentRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ReservationsTest {

    @Autowired
    private ReservationsApi reservationsApi;

    @Autowired
    private MoviesFixtures moviesFixtures;

    @Autowired
    private CinemasFixtures cinemasFixtures;

    @Autowired
    private ShowFixtures showFixtures;

    @Autowired
    private UserFixtures userFixtures;

    @BeforeEach
    void setup() {
        userFixtures.create();
        userFixtures.loginAsAdmin();
        moviesFixtures.create();
        cinemasFixtures.create();
        showFixtures.create();
    }

    @Test
    @SneakyThrows
    void createsAnonymousReservation() {
        // when
        reservationsApi.logOut();
        var createResponse = reservationsApi.createReservation(
            new CreateReservationRequest(showFixtures.batmanMagnoliaWroclawId, Map.of(TicketKind.REGULAR, 2),
                Set.of(new Seat(1, 1), new Seat(1, 2))
            ));

        // then
        createResponse.andExpect(status().is2xxSuccessful());

        // when
        var reservationId = createResponse.getObject(CreateReservationResponse.class).reservationId();
        var reservation = reservationsApi.getReservation(reservationId);

        // then
        assertThat(reservation).isEqualTo(
            new ReservationDto(reservationId, showFixtures.batmanMagnoliaWroclawId, cinemasFixtures.wroclawMagnoliaId,
                cinemasFixtures.hall1WroclawMagnoliaIdId, ReservationStatus.SEATS_RESERVED,
                Map.of(TicketKind.REGULAR, 2), Set.of(new Seat(1, 1), new Seat(1, 2)),
                new ReceiptDto(new BigDecimal("80.00"),
                    List.of(new ReceiptLineDto(TicketKind.REGULAR, 2, new BigDecimal("40.00"), new BigDecimal("80.00")))
                ), null
            ));
    }

    @Test
    @SneakyThrows
    void createsNonAnonymousReservation() {
        // when
        userFixtures.loginAsCustomer();
        var createResponse = reservationsApi.createReservation(
            new CreateReservationRequest(showFixtures.batmanMagnoliaWroclawId, Map.of(TicketKind.REGULAR, 2),
                Set.of(new Seat(1, 1), new Seat(1, 2))
            ));

        // then
        createResponse.andExpect(status().is2xxSuccessful());

        // when
        var reservationId = createResponse.getObject(CreateReservationResponse.class).reservationId();
        var reservation = reservationsApi.getReservation(reservationId);

        // then
        assertThat(reservation).isEqualTo(
            new ReservationDto(reservationId, showFixtures.batmanMagnoliaWroclawId, cinemasFixtures.wroclawMagnoliaId,
                cinemasFixtures.hall1WroclawMagnoliaIdId, ReservationStatus.SEATS_RESERVED,
                Map.of(TicketKind.REGULAR, 2), Set.of(new Seat(1, 1), new Seat(1, 2)),
                new ReceiptDto(new BigDecimal("80.00"),
                    List.of(new ReceiptLineDto(TicketKind.REGULAR, 2, new BigDecimal("40.00"), new BigDecimal("80.00")))
                ), new CustomerInformation(userFixtures.customerId, userFixtures.customer.firstName(),
                userFixtures.customer.lastName(), null, userFixtures.customer.email()
            )
            ));
    }

    @Test
    @SneakyThrows
    void cannotCreateReservationForAlreadyReservedSeats() {
        // given
        userFixtures.loginAsCustomer();
        reservationsApi.createReservation(
            new CreateReservationRequest(showFixtures.batmanMagnoliaWroclawId, Map.of(TicketKind.REGULAR, 2),
                Set.of(new Seat(1, 1), new Seat(1, 2))
            ));

        // when
        reservationsApi.logOut();
        var response = reservationsApi.createReservation(
            new CreateReservationRequest(showFixtures.batmanMagnoliaWroclawId, Map.of(TicketKind.SENIOR, 1),
                Set.of(new Seat(1, 1))
            ));

        // then
        response.andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    void cannotReserveInvalidSeat() {
        // expect
        reservationsApi.createReservation(
            new CreateReservationRequest(showFixtures.batmanMagnoliaWroclawId, Map.of(TicketKind.REGULAR, 2),
                Set.of(new Seat(10000, 1000), new Seat(1, 2))
            )).andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void cannotReserveInvalidShow() {
        reservationsApi.createReservation(
                new CreateReservationRequest(8000L, Map.of(TicketKind.REGULAR, 2), Set.of(new Seat(1, 1), new Seat(1, 2))))
            .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void startsOnlinePayment() {
        // given
        userFixtures.loginAsCustomer();
        var reservationId = reservationsApi.createReservation(
            new CreateReservationRequest(showFixtures.batmanMagnoliaWroclawId, Map.of(TicketKind.REGULAR, 2),
                Set.of(new Seat(1, 1), new Seat(1, 2))
            )).getObject(CreateReservationResponse.class).reservationId();

        // when
        var response = reservationsApi.startOnlinePayment(reservationId, new StartPaymentRequest(null, new RegisteredCustomerInformation("300 300 300")));

        // then
        response.andExpect(status().is2xxSuccessful());
    }
}
