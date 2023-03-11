package pl.com.bottega.cymes.reservations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.showscheduler.ShowSchedulerFacade;
import pl.com.bottega.cymes.showscheduler.dto.ShowDto;

interface ShowProvider {
    ShowDto getShow(Long showId);
}

@Component
@RequiredArgsConstructor
class ShowSchedulerModuleShowProvider implements ShowProvider {

    private final ShowSchedulerFacade showSchedulerFacade;

    @Override
    public ShowDto getShow(Long showId) {
        return showSchedulerFacade.getShow(showId);
    }
}