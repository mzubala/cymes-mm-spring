package pl.com.bottega.cymes.reservations;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import pl.com.bottega.cymes.reservations.dto.ReceiptDto;
import pl.com.bottega.cymes.reservations.dto.ReceiptDto.ReceiptLineDto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;

@Entity
class Receipt {

    @Id
    @Column(name = "reservation_id")
    private UUID reservationId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "total"))
    })
    @Embedded
    private Money total;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReceiptLine> lines = new HashSet<>();

    Money getTotal() {
        return total;
    }

    UUID getReservationId() {
        return reservationId;
    }

    ReceiptDto toDto() {
        return new ReceiptDto(total.getAmount(), lines.stream().map(ReceiptLine::toDto).collect(Collectors.toList()));
    }

    static class Builder {

        private Receipt product = new Receipt();

        Builder withLine(TicketKind ticketKind, Integer count, Money price) {
            product.lines.add(new ReceiptLine(product, ticketKind, price, count));
            return this;
        }

        Receipt build() {
            checkState(product.lines.size() > 0, "Add some lines first!");
            product.total = product.lines.stream().map(rl -> rl.getTotal()).reduce(Money::add).get();
            return product;
        }

        void withReservation(Reservation reservation) {
            product.reservation = reservation;
            product.reservationId = reservation.getId();
        }
    }
}

@Entity
@EqualsAndHashCode
class ReceiptLine {

    @EmbeddedId
    private ReceiptLineId id;

    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "single_ticket_price"))
    })
    @Embedded
    private Money singleTicketPrice;

    private Integer count;

    @ManyToOne
    private Receipt receipt;

    protected ReceiptLine() {
    }

    ReceiptLine(Receipt receipt, TicketKind ticketKind, Money singleTicketPrice, Integer count) {
        this.id = new ReceiptLineId(ticketKind, receipt.getReservationId());
        this.singleTicketPrice = singleTicketPrice;
        this.count = count;
        this.receipt = receipt;
    }

    Money getTotal() {
        return singleTicketPrice.multiply(count);
    }

    ReceiptLineDto toDto() {
        return new ReceiptLineDto(id.getTicketKind(), count, singleTicketPrice.getAmount(), getTotal().getAmount());
    }
}

@Embeddable
@EqualsAndHashCode
class ReceiptLineId implements Serializable {
    private TicketKind ticketKind;
    private UUID reservationId;

    protected ReceiptLineId() {
    }

    ReceiptLineId(TicketKind ticketKind, UUID reservationId) {
        this.ticketKind = ticketKind;
        this.reservationId = reservationId;
    }

    TicketKind getTicketKind() {
        return ticketKind;
    }
}
