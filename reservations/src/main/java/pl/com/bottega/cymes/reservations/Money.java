package pl.com.bottega.cymes.reservations;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkNotNull;

@Embeddable
@EqualsAndHashCode
class Money {

    private BigDecimal amount;

    protected Money() {
    }

    Money(BigDecimal amount) {
        checkNotNull(amount);
        this.amount = amount.setScale(2);
    }

    Money(Integer amount) {
        this(new BigDecimal(amount));
    }

    Money(double amount) {
        this(new BigDecimal(amount));
    }

    Money add(Money other) {
        return new Money(amount.add(other.amount));
    }

    Money multiply(Integer number) {
        return new Money(amount.multiply(new BigDecimal(number)));
    }
}
