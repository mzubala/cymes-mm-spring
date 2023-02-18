package pl.com.bottega.cymes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.commons.test.IntegrationTest;
import pl.com.bottega.cymes.commons.test.TimeFixtures;
import pl.com.bottega.cymes.showscheduler.dto.ShowsGroupDto;
import pl.com.bottega.cymes.showscheduler.requests.ScheduleShowRequest;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.com.bottega.cymes.commons.test.MockMvcAssertions.assertSuccess;

@IntegrationTest
class ShowSchedulerTest {

    @Autowired
    private CinemasFixtures cinemasFixtures;

    @Autowired
    private MoviesFixtures moviesFixtures;

    @Autowired
    private ShowSchedulerApi showSchedulerApi;

    @Autowired
    private TimeFixtures timeFixtures;

    @BeforeEach
    void setup() {
        cinemasFixtures.create();
        moviesFixtures.create();
    }


    @Test
    void schedulesAndReadsGroupedShows() {
        // given

        // when
        var scheduleResponses = List.of(
            showSchedulerApi.schedule(
                new ScheduleShowRequest(cinemasFixtures.lublinPlazaId, cinemasFixtures.hall1LublinPlazaId, moviesFixtures.batmanId, timeFixtures.tomorrowAt(15, 0))
            ),
            showSchedulerApi.schedule(
                new ScheduleShowRequest(cinemasFixtures.lublinPlazaId, cinemasFixtures.hall2LublinPlazaId, moviesFixtures.godfellasId, timeFixtures.tomorrowAt(15, 0))
            ),
            showSchedulerApi.schedule(
                new ScheduleShowRequest(cinemasFixtures.wroclawMagnoliaId, cinemasFixtures.hall1WroclawMagnoliaIdId, moviesFixtures.batmanId, timeFixtures.tomorrowAt(15, 0))
            ),
            showSchedulerApi.schedule(
                new ScheduleShowRequest(cinemasFixtures.wroclawMagnoliaId, cinemasFixtures.hall1WroclawMagnoliaIdId, moviesFixtures.batmanId, timeFixtures.tomorrowAt(18, 0))
            )
        );
        var readShowsInLublin = showSchedulerApi.getShows(cinemasFixtures.lublinPlazaId, timeFixtures.tomorrow());
        var readShowsInWroclaw = showSchedulerApi.getShows(cinemasFixtures.wroclawMagnoliaId, timeFixtures.tomorrow());

        // then
        assertSuccess(scheduleResponses);
        assertThat(readShowsInLublin).usingRecursiveFieldByFieldElementComparatorIgnoringFields("shows.id")
            .containsExactly(
                new ShowsGroupDto(
                    new ShowsGroupDto.MovieDto(moviesFixtures.batmanId, "Batman"),
                    List.of(new ShowsGroupDto.ShowDto(1L, LocalTime.parse("15:00")))
                ),
                new ShowsGroupDto(
                    new ShowsGroupDto.MovieDto(moviesFixtures.godfellasId, "The Godfellas"),
                    List.of(new ShowsGroupDto.ShowDto(2L, LocalTime.parse("15:00")))
                )
            );
        assertThat(readShowsInWroclaw).usingRecursiveFieldByFieldElementComparatorIgnoringFields("shows.id")
            .containsExactly(
                new ShowsGroupDto(
                    new ShowsGroupDto.MovieDto(moviesFixtures.batmanId, "Batman"),
                    List.of(
                        new ShowsGroupDto.ShowDto(1L, LocalTime.parse("15:00")),
                        new ShowsGroupDto.ShowDto(1L, LocalTime.parse("18:00"))
                    )
                )
            );
    }

}