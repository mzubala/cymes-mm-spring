package pl.com.bottega.cymes.reservations;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.commons.application.AuthenticationUtils;
import pl.com.bottega.cymes.commons.rest.GlobalError;
import pl.com.bottega.cymes.reservations.dto.ReservationDto;
import pl.com.bottega.cymes.reservations.request.CreateReservationRequest;
import pl.com.bottega.cymes.reservations.request.CreateReservationResponse;
import pl.com.bottega.cymes.reservations.request.StartPaymentRequest;

import java.util.HashSet;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
class ReservationsController {

    private final ReservationService reservationService;

    @PostMapping
    CreateReservationResponse createReservation(
        @RequestBody @Valid CreateReservationRequest request, Authentication authentication
    ) {
        var id = reservationService.createReservation(
            new CreateReservationCommand(request.showId(), AuthenticationUtils.getUserId(authentication),
                request.ticketCounts(), new HashSet<>(request.seats())
            ));
        return new CreateReservationResponse(id);
    }

    @GetMapping("/{reservationId}")
    ReservationDto getReservation(@PathVariable UUID reservationId) {
        return reservationService.getReservation(reservationId);
    }

    @PostMapping("/{reservationId}/payment/online")
    void startOnlinePayment(@PathVariable UUID reservationId, @RequestBody StartPaymentRequest request) {
        reservationService.startOnlinePayment(
            new StartPaymentCommand(reservationId, request.anonymousCustomerInformation(),
                request.registeredCustomerInformation()
            ));
    }

    @PostMapping("/{reservationId}/payment/onsite")
    void startOnsitePayment(@PathVariable UUID reservationId, @RequestBody StartPaymentRequest request) {
        reservationService.startOnsitePayment(
            new StartPaymentCommand(reservationId, request.anonymousCustomerInformation(),
                request.registeredCustomerInformation()
            ));
    }

    @ExceptionHandler({InvalidReservationParamsException.class, IllegalReservationOperationException.class})
    ResponseEntity<GlobalError> handleInvalidReservationParamsException(Exception ex) {
        return ResponseEntity.badRequest().body(new GlobalError(ex.getMessage()));
    }

}
