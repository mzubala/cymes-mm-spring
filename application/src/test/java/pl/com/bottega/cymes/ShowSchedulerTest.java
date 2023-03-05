package pl.com.bottega.cymes;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.commons.test.FixedClockProvider;
import pl.com.bottega.cymes.commons.test.IntegrationTest;
import pl.com.bottega.cymes.commons.test.TimeFixtures;
import pl.com.bottega.cymes.showscheduler.dto.ShowsGroupDto;
import pl.com.bottega.cymes.showscheduler.requests.ScheduleShowRequest;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.com.bottega.cymes.commons.test.MockMvcAssertions.assertSuccess;

@IntegrationTest
class ShowSchedulerTest {

    @Autowired
    private CinemasFixtures cinemasFixtures;

    @Autowired
    private MoviesFixtures moviesFixtures;

    @Autowired
    private UserFixtures userFixtures;

    @Autowired
    private ShowSchedulerApi showSchedulerApi;

    @Autowired
    private TimeFixtures timeFixtures;

    @Autowired
    private FixedClockProvider fixedClockProvider;

    @BeforeEach
    void setup() {
        userFixtures.create();
        userFixtures.loginAsAdmin();
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
        assertThat(readShowsInLublin).usingRecursiveFieldByFieldElementComparatorIgnoringFields("shows.showId")
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
        assertThat(readShowsInWroclaw).usingRecursiveFieldByFieldElementComparatorIgnoringFields("shows.showId")
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

    @Test
    @SneakyThrows
    void cannotScheduleTwoCollidingShows() {
        // given
        fixedClockProvider.fixAt(timeFixtures.tomorrowAt(10, 0));
        showSchedulerApi.schedule(
            new ScheduleShowRequest(cinemasFixtures.wroclawMagnoliaId, cinemasFixtures.hall1WroclawMagnoliaIdId, moviesFixtures.batmanId, fixedClockProvider.now())
        );

        // when
        var response = showSchedulerApi.schedule(
            new ScheduleShowRequest(cinemasFixtures.wroclawMagnoliaId, cinemasFixtures.hall1WroclawMagnoliaIdId, moviesFixtures.batmanId, fixedClockProvider.now().plus(135, ChronoUnit.MINUTES))
        );

        // then
        response.andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    void requiresAdminUserToScheduleShow() {
        // when
        showSchedulerApi.logOut();
        var request = new ScheduleShowRequest(cinemasFixtures.wroclawMagnoliaId, cinemasFixtures.hall1WroclawMagnoliaIdId, moviesFixtures.batmanId, timeFixtures.tomorrowAt(10, 0));

        // then
        showSchedulerApi.schedule(request).andExpect(status().isForbidden());

        // when
        userFixtures.loginAsCustomer();
        showSchedulerApi.schedule(request).andExpect(status().isForbidden());
    }
}
