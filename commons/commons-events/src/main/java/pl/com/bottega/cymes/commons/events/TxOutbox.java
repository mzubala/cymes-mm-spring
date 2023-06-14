package pl.com.bottega.cymes.commons.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.sharedkernel.ClockProvider;

@Component
@RequiredArgsConstructor
class TxOutbox {

    private final EventsProperties eventsProperties;
    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;

    private final ClockProvider clockProvider;

    // TODO

}

@Component
@RequiredArgsConstructor
class OutboxSender {

    private final RabbitTemplate rabbitTemplate;
    private final ClockProvider clockProvider;

    // TODO

}

@Entity
@Table(name = "outgoing_events")
class OutgoingEvent {

    // TODO

}

interface OutgoingEventRepository extends JpaRepository<OutgoingEvent, Long> {

    // TODO
}
