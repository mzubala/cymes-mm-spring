package pl.com.bottega.cymes.reservations;

import com.payments.paymentsservice.Payment;
import com.payments.paymentsservice.PaymentsService;
import com.payments.paymentsservice.PaymentsService_Service;
import com.payments.paymentsservice.StartPayment;
import com.payments.paymentsservice.StartPaymentResponse;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

interface PaymentsFacade {
    StartedPayment startPayment(UUID reservationId, Money amount);
}

record StartedPayment(String id, URI redirectUri) {

}

class ExternalPaymentGatewayFacade extends WebServiceGatewaySupport implements PaymentsFacade {

    @Override
    @SneakyThrows
    public StartedPayment startPayment(UUID reservationId, Money amount) {
        var request = new StartPayment();
        var payment = new Payment();
        payment.setAssociatedID(reservationId.toString());
        payment.setAmount(amount.getAmount());
        request.setPayment(payment);
        var response = (StartPaymentResponse) getWebServiceTemplate().marshalSendAndReceive(
            request
        );
        return new StartedPayment(response.getID(), new URI(response.getRedirectURI()));
    }
}

@Configuration
class WSConfig {

    @Bean
    Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.payments.paymentsservice");
        return marshaller;
    }

    @Bean
    ExternalPaymentGatewayFacade externalPaymentGatewayFacade(Jaxb2Marshaller jaxb2Marshaller) {
        var facade = new ExternalPaymentGatewayFacade();
        facade.setDefaultUri("http://localhost:9191");
        facade.setMarshaller(jaxb2Marshaller);
        facade.setUnmarshaller(jaxb2Marshaller);
        return facade;
    }

}


