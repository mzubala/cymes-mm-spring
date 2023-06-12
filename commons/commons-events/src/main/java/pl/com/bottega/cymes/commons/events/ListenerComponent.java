package pl.com.bottega.cymes.commons.events;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("rabbitMQInfrastructureInitializer")
public @interface ListenerComponent {
}
