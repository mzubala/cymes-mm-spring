package pl.com.bottega.cymes.reservations;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import pl.com.bottega.cymes.reservations.StartPaymentCommand.AnonymousCustomerInformation;
import pl.com.bottega.cymes.reservations.StartPaymentCommand.RegisteredCustomerInformation;
import pl.com.bottega.cymes.showscheduler.dto.ShowDto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static jakarta.persistence.CascadeType.ALL;
import static pl.com.bottega.cymes.reservations.ReservationStatus.SEATS_RESERVED;
import static pl.com.bottega.cymes.reservations.ReservationStatus.WAITING_ONLINE_PAYMENT;
import static pl.com.bottega.cymes.reservations.ReservationStatus.WAITING_ONSITE_PAYMENT;

@Entity
class Reservation {
    @Id
    private UUID id;

    private Long showId;

    @ElementCollection
    private Map<TicketKind, Integer> ticketCounts = new HashMap<>();

    @ElementCollection
    private Set<SeatEmbeddable> seats = new HashSet<>();

    @Embedded
    private CustomerInformationEmbeddable customerInformation;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @OneToOne(mappedBy = "reservation", orphanRemoval = true, cascade = ALL)
    private Receipt receipt;

    @OneToOne(mappedBy = "reservation", orphanRemoval = true, cascade = ALL)
    private Payment payment;

    @Transient
    private ReceiptCalculator receiptCalculator;

    protected Reservation() {
    }

    Reservation(
        ShowDto showDto, Map<TicketKind, Integer> ticketCounts, Set<Seat> seats, ReceiptCalculator receiptCalculator
    ) {
        this(showDto, (CustomerInformationEmbeddable) null, ticketCounts, seats, receiptCalculator);
    }

    public Reservation(
        ShowDto showDto, CustomerInformation customerInformation, Map<TicketKind, Integer> ticketCounts,
        Set<Seat> seats, ReceiptCalculator receiptCalculator
    ) {
        this(showDto, new CustomerInformationEmbeddable(customerInformation), ticketCounts, seats, receiptCalculator);
    }

    private Reservation(
        ShowDto showDto, CustomerInformationEmbeddable customerInformation, Map<TicketKind, Integer> ticketCounts,
        Set<Seat> seats, ReceiptCalculator receiptCalculator
    ) {
        this.receiptCalculator = receiptCalculator;
        validateParameters(showDto, customerInformation, ticketCounts, seats);
        this.showId = showDto.showId();
        this.customerInformation = customerInformation;
        this.ticketCounts.putAll(ticketCounts);
        this.seats.addAll(seats.stream().map(SeatEmbeddable::new).toList());
        this.status = SEATS_RESERVED;
        this.receipt = receiptCalculator.calculate(this);
    }

    Long getShowId() {
        return showId;
    }

    Long getUserId() {
        if (customerInformation == null) {
            return null;
        }
        else {
            return customerInformation.userId;
        }
    }

    Map<TicketKind, Integer> getTicketCounts() {
        return new HashMap<>(ticketCounts);
    }

    Set<Seat> getSeats() {
        return seats.stream().map(SeatEmbeddable::toSeat).collect(Collectors.toSet());
    }

    UUID getId() {
        return id;
    }

    ReservationStatus getStatus() {
        return status;
    }

    Receipt getReceipt() {
        return receipt;
    }

    Payment getPayment() {
        return payment;
    }

    CustomerInformation getCustomerInfromation() {
        if (customerInformation == null) {
            return null;
        }
        return customerInformation.export();
    }

    void startOnlinePayment(
        String externalPaymentId, AnonymousCustomerInformation anonymousCustomerInformation,
        RegisteredCustomerInformation registeredCustomerInformation
    ) {
        checkStatus(SEATS_RESERVED);
        payment = Payment.createOnlinePayment(this, externalPaymentId);
        updateCustomerInformation(anonymousCustomerInformation, registeredCustomerInformation);
        status = WAITING_ONLINE_PAYMENT;
    }

    void startOnsitePayment(
        AnonymousCustomerInformation anonymousCustomerInformation, RegisteredCustomerInformation registeredCustomerInformation
    ) {
        checkStatus(SEATS_RESERVED);
        payment = Payment.createOnsitePayment(this);
        updateCustomerInformation(anonymousCustomerInformation, registeredCustomerInformation);
        status = WAITING_ONSITE_PAYMENT;
    }

    private void updateCustomerInformation(
        AnonymousCustomerInformation anonymousCustomerInformation,
        RegisteredCustomerInformation registeredCustomerInformation
    ) {
        if (customerInformation == null) {
            customerInformation = new CustomerInformationEmbeddable();
        }
        customerInformation.update(anonymousCustomerInformation, registeredCustomerInformation);
    }

    private static void checkParameter(boolean expression, String errorMessage) {
        if (!expression) {
            throw new InvalidReservationParamsException(errorMessage);
        }
    }

    private Integer totalNumberOfTickets(Map<TicketKind, Integer> ticketCounts) {
        return ticketCounts.entrySet().stream().mapToInt(entry -> {
            checkParameter(entry.getValue() > 0, "Each ticket count must be greater than zero");
            return entry.getValue();
        }).sum();
    }

    private void validateParameters(
        ShowDto showDto, CustomerInformationEmbeddable customerInformation, Map<TicketKind, Integer> ticketCounts,
        Set<Seat> seats
    ) {
        checkParameter(showDto != null, "Show must be defined");
        checkParameter(seats != null, "Seats must be defined");
        checkParameter(ticketCounts != null, "Ticket count must be defined");
        if (customerInformation != null) {
            checkParameter(customerInformation.userId != null, "User must be defined");
            checkParameter(customerInformation.firstName != null, "Customer first name must be defined");
            checkParameter(customerInformation.lastName != null, "Customer last name must be defined");
        }
        Integer numberOfTickets = totalNumberOfTickets(ticketCounts);
        checkParameter(numberOfTickets > 0, "Number of tickets must be non-zero");
        checkParameter(numberOfTickets.equals(seats.size()),
            "There should be the same number of ticekts and seats selected"
        );
    }

    private void checkStatus(ReservationStatus requiredStatus) {
        if (status != requiredStatus) {
            throw new IllegalReservationOperationException(
                "Reservation status is illegal to perform desired operation " + requiredStatus);
        }
    }

    private boolean isAnonymous() {
        return customerInformation.isAnonymous();
    }

    @Embeddable
    static class SeatEmbeddable {

        private int rowNumber;
        private int seatNumber;

        SeatEmbeddable(Seat seat) {
            this.rowNumber = seat.rowNumber();
            this.seatNumber = seat.seatNumber();
        }

        protected SeatEmbeddable() {
        }

        Seat toSeat() {
            return new Seat(rowNumber, seatNumber);
        }
    }

    @Embeddable
    @Access(AccessType.FIELD)
    static class CustomerInformationEmbeddable {
        private Long userId;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String email;

        protected CustomerInformationEmbeddable() {
        }

        CustomerInformationEmbeddable(CustomerInformation customerInformation) {
            userId = customerInformation.userId();
            firstName = customerInformation.firstName();
            lastName = customerInformation.lastName();
            phoneNumber = customerInformation.phoneNumber();
            email = customerInformation.email();
        }

        boolean isAnonymous() {
            return userId == null;
        }

        void update(
            AnonymousCustomerInformation anonymousCustomerInformation,
            RegisteredCustomerInformation registeredCustomerInformation
        ) {
            if (isAnonymous()) {
                update(anonymousCustomerInformation);
            }
            else {
                update(registeredCustomerInformation);
            }
        }

        private void update(RegisteredCustomerInformation customerInformation) {
            checkParameter(customerInformation != null, "Missing anonymous customer information");
            phoneNumber = customerInformation.phoneNumber();
        }

        private void update(AnonymousCustomerInformation customerInformation) {
            checkParameter(customerInformation != null, "Missing anonymous customer information");
            firstName = customerInformation.firstName();
            lastName = customerInformation.lastName();
            phoneNumber = customerInformation.phoneNumber();
            email = customerInformation.email();
        }

        CustomerInformation export() {
            return new CustomerInformation(userId, firstName, lastName, phoneNumber, email);
        }
    }
}

enum ReservationStatus {
    DRAFT, SEATS_RESERVED, WAITING_ONLINE_PAYMENT, WAITING_ONSITE_PAYMENT, PAID, CANCELED
}

class InvalidReservationParamsException extends RuntimeException {
    InvalidReservationParamsException(String message) {
        super(message);
    }
}

class IllegalReservationOperationException extends RuntimeException {
    IllegalReservationOperationException(String message) {
        super(message);
    }
}