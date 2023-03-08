package pl.com.bottega.cymes.reservations;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import pl.com.bottega.cymes.commons.test.Faker;
import pl.com.bottega.cymes.showscheduler.dto.ShowDto;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@With
@AllArgsConstructor
@NoArgsConstructor
class ReservationBuilder {
    Long showId = 1L;
    Long userId = 1L;
    Map<TicketKind, Integer> ticketCounts = Map.of(TicketKind.REGULAR, 2);
    Set<Seat> seats = Set.of(new Seat(10, 10), new Seat(10, 11));

    CustomerInformation customerInformation = null;

    static ReservationBuilder aReservation() {
        return new ReservationBuilder();
    }

    Reservation build() {
        if(customerInformation == null) {
            return new Reservation(showId, ticketCounts, seats);
        } else {
            return new Reservation(showId, customerInformation, ticketCounts, seats);
        }
    }
}

@AllArgsConstructor
@NoArgsConstructor
@With
class ShowDtoBuilder {
    Long showId = 1L;
    Long cinemaId = 1L;
    Long cinemaHallId = 1L;
    Instant when = Instant.now();

    static ShowDtoBuilder aShowDto() {
        return new ShowDtoBuilder();
    }

    ShowDto build() {
        return new ShowDto(showId, cinemaId, cinemaHallId, when);
    }
}

class CustomerInformationBuilder {

    Long userId = 1L;
    String firstName = new Faker().name().firstName();
    String lastName = new Faker().name().lastName();
    String phoneNumber = new Faker().phoneNumber().phoneNumber();
    String email = new Faker().internet().emailAddress();

    CustomerInformationBuilder aCustomerInformation() {
        return new CustomerInformationBuilder();
    }

    CustomerInformation build() {
        return new CustomerInformation(userId, firstName, lastName, phoneNumber, email);
    }
}