package pl.com.bottega.cymes.reservations;

import org.springframework.data.repository.Repository;

import java.util.UUID;

interface ReservationRepository extends Repository<Reservation, UUID> {
    Reservation getReferenceById(UUID id);

    void save(Reservation reservation);
}