package pl.com.bottega.cymes.reservations.dto;

import pl.com.bottega.cymes.reservations.PaymentMethod;

public record PaymentDto(
    PaymentMethod method,
    boolean completed
) {
}
