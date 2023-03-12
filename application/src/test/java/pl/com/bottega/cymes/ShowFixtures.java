package pl.com.bottega.cymes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.commons.test.TimeFixtures;

@Component
@RequiredArgsConstructor
class ShowFixtures {

    Long batmanMagnoliaWroclawId;

    private final ShowSchedulerApi showSchedulerApi;

    private final MoviesFixtures moviesFixtures;

    private final CinemasFixtures cinemasFixtures;

    private final TimeFixtures timeFixtures;

    void create() {
        // TODO
    }
}
