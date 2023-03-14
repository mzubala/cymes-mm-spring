package pl.com.bottega.cymes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.com.bottega.cymes.reservations.dto.ReservationDto;
import pl.com.bottega.cymes.reservations.request.CreateReservationRequest;
import pl.com.bottega.cymes.reservations.request.StartPaymentRequest;

import java.util.UUID;

@Component
class ReservationsApi extends Api {
    public ReservationsApi(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    ExtendedResultActions createReservation(CreateReservationRequest request) {
        return post("/reservations", request);
    }

    ReservationDto getReservation(UUID reservationId) {
        return getObject("/reservations/{id}", ReservationDto.class, reservationId);
    }

    ResultActions startOnlinePayment(UUID reservationId, StartPaymentRequest startPaymentRequest) {
        return post("/reservations/{id}/payment/online", startPaymentRequest, reservationId.toString());
    }
}
