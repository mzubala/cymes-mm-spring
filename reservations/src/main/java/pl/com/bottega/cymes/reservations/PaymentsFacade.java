package pl.com.bottega.cymes.reservations;

import com.payments.paymentsservice.Payment;
import com.payments.paymentsservice.StartPayment;
import com.payments.paymentsservice.StartPaymentResponse;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.net.URI;
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
@EnableConfigurationProperties({PaymentsConfiguration.class})
class WSConfig {

    @Bean
    Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.payments.paymentsservice");
        return marshaller;
    }

    @Bean
    @Profile({"!integration"})
    ExternalPaymentGatewayFacade externalPaymentGatewayFacade(Jaxb2Marshaller jaxb2Marshaller, PaymentsConfiguration paymentsConfiguration) {
        var facade = new ExternalPaymentGatewayFacade();
        facade.setDefaultUri(paymentsConfiguration.url());
        facade.setMarshaller(jaxb2Marshaller);
        facade.setUnmarshaller(jaxb2Marshaller);
        return facade;
    }

}

@ConfigurationProperties(prefix = "services.payments")
record PaymentsConfiguration(String url) {

}

