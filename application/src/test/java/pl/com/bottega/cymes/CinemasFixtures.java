package pl.com.bottega.cymes;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.cinemas.RowElementKind;
import pl.com.bottega.cymes.cinemas.dto.RowDto;
import pl.com.bottega.cymes.cinemas.dto.RowElementDto;
import pl.com.bottega.cymes.cinemas.requests.CreateCinemaHallRequest;
import pl.com.bottega.cymes.cinemas.requests.CreateCinemaRequest;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.com.bottega.cymes.commons.test.MockMvcAssertions.assertSuccess;
import static pl.com.bottega.cymes.CreateCinemaHallRequestExample.aCreateCinemaHallRequest;

@Component
@RequiredArgsConstructor
class CinemasFixtures {

    private final CinemasApi cinemasApi;

    Long warszawaArkadiaId;
    Long wroclawMagnoliaId;
    Long lublinPlazaId;

    Long hall1LublinPlazaId;
    Long hall2LublinPlazaId;
    Long hall1WarszawaArkadiaId;
    Long hall1WroclawMagnoliaIdId;

    void create() {
        createCinemas();
        createCinemaHalls();
    }

    private void createCinemas() {
        assertSuccess(
            cinemasApi.create(new CreateCinemaRequest("Warszawa", "Arkadia")),
            cinemasApi.create(new CreateCinemaRequest("Wroclaw", "Magnolia")),
            cinemasApi.create(new CreateCinemaRequest("Lublin", "Plaza"))
        );
        warszawaArkadiaId = cinemasApi.getCinemaId("Warszawa", "Arkadia");
        wroclawMagnoliaId = cinemasApi.getCinemaId("Wroclaw", "Magnolia");
        lublinPlazaId = cinemasApi.getCinemaId("Lublin", "Plaza");
    }

    private void createCinemaHalls() {
        assertSuccess(
            cinemasApi.createCinemaHall(aCreateCinemaHallRequest(lublinPlazaId).withName("H1").build()),
            cinemasApi.createCinemaHall(aCreateCinemaHallRequest(lublinPlazaId).withName("H2").build()),
            cinemasApi.createCinemaHall(aCreateCinemaHallRequest(warszawaArkadiaId).withName("H1").build()),
            cinemasApi.createCinemaHall(aCreateCinemaHallRequest(wroclawMagnoliaId).withName("H1").build())
        );
        hall1LublinPlazaId = cinemasApi.getCinemaDetails(lublinPlazaId).getHall("H1").id();
        hall2LublinPlazaId = cinemasApi.getCinemaDetails(lublinPlazaId).getHall("H2").id();
        hall1WroclawMagnoliaIdId = cinemasApi.getCinemaDetails(wroclawMagnoliaId).getHall("H1").id();
        hall1WarszawaArkadiaId = cinemasApi.getCinemaDetails(warszawaArkadiaId).getHall("H1").id();
    }
}

@With
@AllArgsConstructor
@NoArgsConstructor
class CreateCinemaHallRequestExample {

    Long cinemaId;
    String name = "H01";

    Integer rowsNumber = 4;

    Integer seatsPerRow = 5;

    static CreateCinemaHallRequestExample aCreateCinemaHallRequest(Long cinemaId) {
        return new CreateCinemaHallRequestExample().withCinemaId(cinemaId);
    }

    CreateCinemaHallRequest build() {
        return new CreateCinemaHallRequest(cinemaId, name,
            IntStream.range(1, rowsNumber + 1)
                .mapToObj(rowsNumber -> new RowDto(rowsNumber, IntStream.range(1, seatsPerRow + 1)
                    .mapToObj(seatNumber -> new RowElementDto(seatNumber - 1, seatNumber, RowElementKind.SEAT))
                    .collect(Collectors.toList())
                )).collect(Collectors.toList()));
    }
}