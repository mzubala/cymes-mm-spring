package pl.com.bottega.cymes;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.commons.test.IntegrationTest;

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
        // TODO
    }

    @Test
    @SneakyThrows
    void createsNonAnonymousReservation() {
        // TODO
    }

    @Test
    @SneakyThrows
    void cannotCreateReservationForAlreadyReservedSeats() {
        // TODO
    }

    @Test
    @SneakyThrows
    void cannotReserveInvalidSeat() {
        // TODO
    }

    @Test
    @SneakyThrows
    void cannotReserveInvalidShow() {
        // TODO
    }
}
