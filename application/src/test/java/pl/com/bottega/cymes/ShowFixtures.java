package pl.com.bottega.cymes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.commons.test.TimeFixtures;
import pl.com.bottega.cymes.showscheduler.requests.ScheduleShowRequest;

@Component
@RequiredArgsConstructor
class ShowFixtures {

    Long batmanMagnoliaWroclawId;

    private final ShowSchedulerApi showSchedulerApi;

    private final MoviesFixtures moviesFixtures;

    private final CinemasFixtures cinemasFixtures;

    private final TimeFixtures timeFixtures;

    void create() {
        showSchedulerApi.schedule(
            new ScheduleShowRequest(cinemasFixtures.wroclawMagnoliaId, cinemasFixtures.hall1WroclawMagnoliaIdId,
                moviesFixtures.batmanId, timeFixtures.tomorrowAt(15, 0)
            ));
        batmanMagnoliaWroclawId = showSchedulerApi.getShows(cinemasFixtures.wroclawMagnoliaId, timeFixtures.tomorrow())
            .get(0).shows().get(0).showId();
    }
}
