package pl.com.bottega.cymes.reservations;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.UUID;

interface PaymentsFacade {
    StartedPayment startPayment(UUID reservationId, Money amount);
}

record StartedPayment(String id, URI redirectUri) {

}

@Component
class ExternalPaymentGatewayFacade implements PaymentsFacade {

    @Override
    @SneakyThrows
    public StartedPayment startPayment(UUID reservationId, Money amount) {
        return new StartedPayment(UUID.randomUUID().toString(), new URI("http://todo.com/" + reservationId));
    }
}
