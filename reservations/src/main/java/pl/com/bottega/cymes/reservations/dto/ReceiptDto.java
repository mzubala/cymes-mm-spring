package pl.com.bottega.cymes.reservations.dto;

import pl.com.bottega.cymes.reservations.TicketKind;

import java.math.BigDecimal;
import java.util.List;

public record ReceiptDto(
    BigDecimal total,
    List<ReceiptLineDto> lines
) {
    public record ReceiptLineDto(
        TicketKind ticketKind,
        Integer count,
        BigDecimal price,
        BigDecimal total
    ) {

    }
}
