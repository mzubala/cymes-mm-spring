package pl.com.bottega.cymes.reservations;


import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import pl.com.bottega.cymes.showscheduler.dto.ShowDto;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

abstract class InMemoryDb<T, ID> {
    private final Map<ID, T> db = new HashMap<>();

    void save(ID id, T t) {
        db.put(id, t);
    }

    T get(ID id) {
        var t = db.get(id);
        if (t == null) {
            throw new EntityNotFoundException();
        }
        return t;
    }
}

class InMemoryReservationRepository extends InMemoryDb<Reservation, UUID> implements ReservationRepository {
    @Override
    public Reservation getReferenceById(UUID id) {
        return get(id);
    }

    @Override
    public void save(Reservation reservation) {
        save(reservation.getId(), reservation);
    }
}

class InMemoryShowProvider extends InMemoryDb<ShowDto, Long> implements ShowProvider {

    @Override
    public ShowDto getShow(Long showId) {
        return get(showId);
    }
}

class InMemoryCustomerInformationProvider extends InMemoryDb<CustomerInformation, Long>
    implements CustomerInformationProvider {

    @Override
    public CustomerInformation getByUserId(Long userId) {
        return get(userId);
    }
}

class InMemoryCinemaHallProvider extends InMemoryDb<CinemaHall, Long> implements CinemaHallProvider {

    @Override
    public CinemaHall getById(Long cinemaHallId) {
        return get(cinemaHallId);
    }
}

class FakePaymentFacade implements PaymentsFacade {

    private StartedPayment lastStartedPayment;
    private Money lastStartedPaymentAmount;

    @SneakyThrows
    @Override
    public StartedPayment startPayment(UUID reservationId, Money amount) {
        lastStartedPayment = new StartedPayment(UUID.randomUUID().toString(), new URI("http://test.com"));
        lastStartedPaymentAmount = amount;
        return lastStartedPayment;
    }

    StartedPayment getLastStartedPayment() {
        return lastStartedPayment;
    }

    Money getLastStartedPaymentAmount() {
        return lastStartedPaymentAmount;
    }
}