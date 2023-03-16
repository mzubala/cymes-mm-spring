package pl.com.bottega.cymes.commons.test;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.sharedkernel.Event;
import pl.com.bottega.cymes.sharedkernel.EventsPublisher;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Primary
@Component
class FakeEventPublisher implements EventsPublisher {

    private final List<Event> publishedEvents = new LinkedList<>();

    @Override
    public void publish(Event event) {
        publishedEvents.add(event);
    }

    public void assertPublished(Event event) {
        assertThat(publishedEvents).contains(event);
    }

    public void assertPublishedExactly(Event... events) {
        assertThat(publishedEvents).containsExactly(events);
    }
}
