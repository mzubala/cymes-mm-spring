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
        var startedPayment = new StartedPaymentWrapper(
            new StartedPayment(UUID.randomUUID().toString(), new URI("http://payments.com/" + reservationId)),
            reservationId, amount
        );
        startedPayments.add(startedPayment);
        return startedPayment.payment;
    }

    public boolean isPaymentCreatedFor(UUID reservationId, BigDecimal amount) {
        var moneyAmount = new Money(amount);
        return startedPayments.stream()
            .anyMatch((p) -> p.reservationId.equals(reservationId) && p.amount.equals(moneyAmount));
    }

    private record StartedPaymentWrapper(StartedPayment payment, UUID reservationId, Money amount) {
    }
}
