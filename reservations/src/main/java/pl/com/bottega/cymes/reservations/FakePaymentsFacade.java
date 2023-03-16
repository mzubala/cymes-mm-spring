package pl.com.bottega.cymes.reservations;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Profile("integration")
@Component
public class FakePaymentsFacade implements PaymentsFacade {

    private final List<StartedPaymentWrapper> startedPayments = new LinkedList<>();

    @Override
    @SneakyThrows
    public StartedPayment startPayment(UUID reservationId, Money amount) {
        // TODO
        return null;
    }

    public boolean isPaymentCreatedFor(UUID reservationId, BigDecimal amount) {
        // TODO
        return false;
    }

    private record StartedPaymentWrapper(StartedPayment payment, UUID reservationId, Money amount) {
    }
}
