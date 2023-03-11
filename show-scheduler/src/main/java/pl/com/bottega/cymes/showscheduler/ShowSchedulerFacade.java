package pl.com.bottega.cymes.showscheduler;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.showscheduler.dto.ShowDto;

@Component
@RequiredArgsConstructor
public class ShowSchedulerFacade {

    private final ShowRepository showRepository;

    public ShowDto getShow(Long showId) {
        return showRepository.findById(showId, ShowDto.class)
            .orElseThrow(() -> new EntityNotFoundException("No such show " + showId));
    }

}
