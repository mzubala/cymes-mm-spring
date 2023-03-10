package pl.com.bottega.cymes.reservations;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

import java.util.UUID;

@Entity
class Payment {

    @Id
    private UUID reservationId;

    @OneToOne
    @MapsId("reservationId")
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private String externalId;

    private boolean completed = false;


    protected Payment() {
    }

    private Payment(Reservation reservation, PaymentMethod method, String externalId) {
        this(reservation, method, externalId, false);
    }

    private Payment(Reservation reservation, PaymentMethod online) {
        this(reservation, online, null);
    }

    public Payment(Reservation reservation, PaymentMethod method, String externalId, boolean completed) {
        this.reservation = reservation;
        this.method = method;
        this.externalId = externalId;
        this.completed = completed;
    }

    static Payment createOnlinePayment(Reservation reservation, String externalId) {
        return new Payment(reservation, PaymentMethod.ONLINE, externalId);
    }

    static Payment createOnsitePayment(Reservation reservation) {
        return new Payment(reservation, PaymentMethod.ONLINE);
    }

    Payment complete() {
        return new Payment(reservation, method, externalId, true);
    }

    boolean isCompleted() {
        return completed;
    }

    PaymentMethod getMethod() {
        return method;
    }

    String getExternalId() {
        return externalId;
    }
}

enum PaymentMethod {
    ONLINE, ONSITE
}
