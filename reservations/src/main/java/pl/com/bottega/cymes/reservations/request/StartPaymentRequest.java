package pl.com.bottega.cymes.reservations.request;

import pl.com.bottega.cymes.reservations.dto.AnonymousCustomerInformation;
import pl.com.bottega.cymes.reservations.dto.RegisteredCustomerInformation;

public record StartPaymentRequest(
    AnonymousCustomerInformation anonymousCustomerInformation,
    RegisteredCustomerInformation registeredCustomerInformation
) {
}
