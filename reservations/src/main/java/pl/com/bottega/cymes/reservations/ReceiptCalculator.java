package pl.com.bottega.cymes.reservations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

interface ReceiptCalculator {

    Receipt calculate(Reservation reservation);

}

@Component
class SimpleReceiptCalculator implements ReceiptCalculator {

    private final Map<TicketKind, BigDecimal> ticketPrices;

    SimpleReceiptCalculator(@Value("${ticket-prices}") Map<TicketKind, BigDecimal> ticketPrices) {
        this.ticketPrices = ticketPrices;
    }

    @Override
    public Receipt calculate(Reservation reservation) {
        var receipt = new Receipt.Builder();
        reservation.getTicketCounts().entrySet().forEach(entry -> {
            receipt.withLine(entry.getKey(), entry.getValue(), new Money(ticketPrices.get(entry.getKey())));
        });
        return receipt.build();
    }
}