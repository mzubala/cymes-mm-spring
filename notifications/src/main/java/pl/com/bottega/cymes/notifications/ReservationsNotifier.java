package pl.com.bottega.cymes.notifications;

import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.commons.events.ListenerComponent;
import pl.com.bottega.cymes.reservations.events.ReservationStatusChanged;

@ListenerComponent
@Log
class ReservationsNotifier {

    @RabbitListener(queues = {"pl.com.bottega.cymes.notifications.ReservationsNotifier"})
    void handleReservationStatusChanged(ReservationStatusChanged statusChanged) {
        log.info(statusChanged.toString());
    }

}
