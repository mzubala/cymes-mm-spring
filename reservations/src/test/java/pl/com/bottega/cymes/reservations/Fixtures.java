package pl.com.bottega.cymes.reservations;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import pl.com.bottega.cymes.commons.test.Faker;
import pl.com.bottega.cymes.showscheduler.dto.ShowDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static pl.com.bottega.cymes.reservations.RecipeCalculatorFixtures.defaultReceiptCalculator;
import static pl.com.bottega.cymes.reservations.ShowDtoBuilder.aShowDto;

@With
@AllArgsConstructor
@NoArgsConstructor
class ReservationBuilder {
    ShowDtoBuilder showDto = aShowDto();
    CustomerInformationBuilder customerInformation = null;
    Map<TicketKind, Integer> ticketCounts = Map.of(TicketKind.REGULAR, 2);
    Set<Seat> seats = Set.of(new Seat(10, 10), new Seat(10, 11));

    ReceiptCalculator receiptCalculator = defaultReceiptCalculator();

    static ReservationBuilder aReservation() {
        return new ReservationBuilder();
    }

    Reservation build() {
        if (customerInformation == null) {
            return new Reservation(showDto == null ? null : showDto.build(), ticketCounts, seats, receiptCalculator);
        }
        else {
            return new Reservation(showDto.build(), customerInformation == null ? null : customerInformation.build(),
                ticketCounts, seats, receiptCalculator
            );
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

@NoArgsConstructor
@AllArgsConstructor
@With
class CustomerInformationBuilder {

    Long userId = 1L;
    String firstName = new Faker().name().firstName();
    String lastName = new Faker().name().lastName();
    String phoneNumber = new Faker().phoneNumber().phoneNumber();
    String email = new Faker().internet().emailAddress();

    static CustomerInformationBuilder aCustomerInformation() {
        return new CustomerInformationBuilder();
    }

    CustomerInformation build() {
        return new CustomerInformation(userId, firstName, lastName, phoneNumber, email);
    }
}

@AllArgsConstructor
@NoArgsConstructor
@With
class CinemaHallBuilder {
    Long cinemaHallId = 1L;
    Map<Integer, Integer> rowNumberToSeatCount = new HashMap<>();

    static CinemaHallBuilder aCinemaHall() {
        var builder = new CinemaHallBuilder();
        builder.rowNumberToSeatCount.put(1, 10);
        builder.rowNumberToSeatCount.put(2, 10);
        return builder;
    }

    CinemaHallBuilder withRow(int number, int seatsCount) {
        rowNumberToSeatCount.put(number, seatsCount);
        return this;
    }

    CinemaHall build() {
        return new CinemaHall(cinemaHallId, rowNumberToSeatCount);
    }
}

class RecipeCalculatorFixtures {
    static ReceiptCalculator defaultReceiptCalculator() {
        Map<TicketKind, BigDecimal> priceMap = new HashMap<>();
        Arrays.stream(TicketKind.values()).forEach((v) -> priceMap.put(v, BigDecimal.TEN));
        return new SimpleReceiptCalculator(priceMap);
    }
}