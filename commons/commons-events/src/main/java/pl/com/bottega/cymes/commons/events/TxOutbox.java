package pl.com.bottega.cymes.commons.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.com.bottega.cymes.sharedkernel.ClockProvider;
import pl.com.bottega.cymes.sharedkernel.Event;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
class TxOutbox {

    private final EventsProperties eventsProperties;
    private final ApplicationEventPublisher publisher;
    private final OutgoingEventRepository repository;
    private final ObjectMapper objectMapper;

    private final ClockProvider clockProvider;

    @EventListener
    @SneakyThrows
    public void publishOutgoingEvent(Event event) {
        var outgoingEvent = new OutgoingEvent(
            objectMapper.writeValueAsString(event), event.getClass().getName(), clockProvider.now());
        repository.save(outgoingEvent);
        publisher.publishEvent(outgoingEvent);
    }

}

@Component
@RequiredArgsConstructor
class OutboxSender {

    private final RabbitTemplate rabbitTemplate;
    private final OutgoingEventRepository repository;

    private final ClockProvider clockProvider;

    @TransactionalEventListener
    public void sendOutgoingEvent(OutgoingEvent event) {
        rabbitTemplate.convertAndSend(event.getDestination(), null, event.getPayload());
        repository.deleteById(event.getId());
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    @Transactional
    void retrySending() {
        repository.findFirstByCreatedAtBefore(clockProvider.now().minus(1, ChronoUnit.SECONDS))
            .ifPresent(this::sendOutgoingEvent);
    }

}

@Entity
@Table(name = "outgoing_events")
class OutgoingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4096)
    private String payload;

    private String destination;

    private Instant createdAt;

    protected OutgoingEvent() {
    }

    OutgoingEvent(String payload, String destination, Instant createdAt) {
        this.payload = payload;
        this.destination = destination;
        this.createdAt = createdAt;
    }

    String getPayload() {
        return payload;
    }

    String getDestination() {
        return destination;
    }

    Long getId() {
        return id;
    }
}

interface OutgoingEventRepository extends JpaRepository<OutgoingEvent, Long> {

    @Query(value = "SELECT * FROM outgoing_events WHERE created_at < :at FOR UPDATE SKIP LOCKED", nativeQuery = true)
    Optional<OutgoingEvent> findFirstByCreatedAtBefore(Instant at);
}
