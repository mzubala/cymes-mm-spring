package pl.com.bottega.cymes.commons.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

class RabbitMqExtensions implements BeforeAllCallback {

    private static final RabbitMQContainer CONTAINER = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.7.25-management-alpine"));

    @BeforeAll
    public void beforeAll(ExtensionContext context) {
        startRabbitMq();
    }

    private void startRabbitMq() {
        if (!CONTAINER.isRunning()) {
            CONTAINER.start();
            setRabbitmqEnvVariables();
        }
    }

    private void setRabbitmqEnvVariables() {
        System.setProperty("spring.rabbitmq.host", CONTAINER.getHost());
        System.setProperty("spring.rabbitmq.port", CONTAINER.getAmqpPort().toString());
        System.setProperty("spring.rabbitmq.username", CONTAINER.getAdminUsername());
        System.setProperty("spring.rabbitmq.password", CONTAINER.getAdminPassword());
    }
}

/*class RabbitMqQueuesCleaner implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        val applicationContext = context.applicationContext();
        applicationContext.getBeansOfType(ListenerConfig::class.java).forEach(this::clean)
    }

    private fun clean(ListenerConfig listenerConfig) {
        CONTAINER.execInContainer("rabbitmqadmin", "purge", "queue", "name=${listenerConfig.queue}")
    }
}*/


