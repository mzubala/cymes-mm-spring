package pl.com.bottega.cymes.commons.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.sharedkernel.Event;

@Component
@RequiredArgsConstructor
class TxOutbox {

    private final EventsProperties eventsProperties;
    private final ApplicationEventPublisher publisher;

    @EventListener
    public void publishOutgoingEvent(Event event) {

    }

}
