package pl.com.bottega.cymes.reservations;


import jakarta.persistence.EntityNotFoundException;
import pl.com.bottega.cymes.showscheduler.dto.ShowDto;

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
        if(t == null) {
            throw new EntityNotFoundException();
        }
        return t;
    }
}

class InMemoryReservationRepository extends InMemoryDb<Reservation, UUID> implements ReservationRepository {
    @Override
    public Reservation getReferenceById(UUID id) {
        return null;
    }
}

class InMemoryShowProvider extends InMemoryDb<ShowDto, Long> implements ShowProvider {

    @Override
    public ShowDto getShow(Long showId) {
        return null;
    }
}

class InMemoryCustomerInformationProvider extends InMemoryDb<CustomerInformation, Long> implements CustomerInformationProvider {

    @Override
    public CustomerInformation getByUserId(Long userId) {
        return null;
    }
}
