package pl.com.bottega.cymes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ShowFixtures {

    Long batmanMagnoliaWroclawId;

    private final ShowSchedulerApi showSchedulerApi;

    void create() {

    }
}
