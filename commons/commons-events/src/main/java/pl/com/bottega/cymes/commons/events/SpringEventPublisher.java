package pl.com.bottega.cymes.commons.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.sharedkernel.Event;
import pl.com.bottega.cymes.sharedkernel.EventsPublisher;

@Component
@RequiredArgsConstructor
class SpringEventPublisher implements EventsPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(Event event) {
        applicationEventPublisher.publishEvent(event);
    }
}
