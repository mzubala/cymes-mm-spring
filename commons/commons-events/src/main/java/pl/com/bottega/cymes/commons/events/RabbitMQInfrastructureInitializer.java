package pl.com.bottega.cymes.commons.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.commons.events.EventsProperties.ListenerProperties;
import pl.com.bottega.cymes.commons.events.EventsProperties.TopicProperties;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Log
class RabbitMQInfrastructureInitializer implements InitializingBean {

    private final AmqpAdmin amqpAdmin;

    private final EventsProperties eventsProperties;

    @Override
    public void afterPropertiesSet() {
        createRabbitMqInfrastructure();
    }

    private void createRabbitMqInfrastructure() {
        log.info("Creating RabbitMQ infrastructure");
        eventsProperties.topics().forEach(this::createTopic);
    }

    private void createTopic(TopicProperties topicProperties) {
        log.info("Creating topic " + topicProperties);
        var exchange = new FanoutExchange(topicProperties.name(), true, false);
        amqpAdmin.declareExchange(exchange);
        topicProperties.listeners().forEach(listener -> {
            createListener(exchange, listener);
        });
    }

    private void createListener(FanoutExchange exchange, ListenerProperties listener) {
        var dlxExchange = new DirectExchange(exchange.getName() + ".dlx", true, false);
        amqpAdmin.declareExchange(dlxExchange);
        var queue = new Queue(listener.name(), true, false, false, Map.of("x-dead-letter-exchange", dlxExchange.getName()));
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange));
        var dlq = new Queue(listener.name() + ".dlq", true, false, false);
        amqpAdmin.declareQueue(dlq);
        amqpAdmin.declareBinding(BindingBuilder.bind(dlq).to(dlxExchange).withQueueName());
    }
}
