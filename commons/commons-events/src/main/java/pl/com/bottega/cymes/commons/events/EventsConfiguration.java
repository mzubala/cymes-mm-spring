package pl.com.bottega.cymes.commons.events;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties({EventsProperties.class})
class EventsConfiguration {
}

@ConfigurationProperties(prefix = "events")
record EventsProperties(List<TopicProperties> topics) {
    record TopicProperties(
        String name, List<ListenerProperties> listeners
    ) {
    }

    record ListenerProperties(String name) {
    }
}
