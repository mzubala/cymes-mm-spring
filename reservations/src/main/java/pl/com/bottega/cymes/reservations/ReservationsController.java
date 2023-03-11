package pl.com.bottega.cymes.reservations;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.commons.application.AuthenticationUtils;
import pl.com.bottega.cymes.reservations.request.CreateReservationRequest;
import pl.com.bottega.cymes.reservations.request.CreateReservationResponse;

import java.util.HashSet;

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

}
