package pl.com.bottega.cymes.reservations;

import com.payments.paymentsservice.Payer;
import com.payments.paymentsservice.Payment;
import com.payments.paymentsservice.StartPayment;
import com.payments.paymentsservice.StartPaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import pl.com.bottega.cymes.reservations.dto.AnonymousCustomerInformation;

import java.net.URI;
import java.util.UUID;

interface PaymentsFacade {
    StartedPayment startPayment(UUID reservationId, Money amount);

    StartedPayment startPayment(
        UUID reservationId, AnonymousCustomerInformation anonymousCustomerInformation, Money amount
    );
}

record StartedPayment(String id, URI redirectUri) {

}

@RequiredArgsConstructor
class ExternalPaymentGatewayFacade extends WebServiceGatewaySupport implements PaymentsFacade {

    private final ReservationRepository reservationRepository;

    @Override
    @SneakyThrows
    public StartedPayment startPayment(UUID reservationId, Money amount) {
        var request = new StartPayment();
        var payment = new Payment();
        payment.setAssociatedID(reservationId.toString());
        payment.setAmount(amount.getAmount());
        request.setPayment(payment);
        var reservation = reservationRepository.getReferenceById(reservationId);
        var customerInfo = reservation.getCustomerInfromation();
        var payer = new Payer();
        payer.setEmail(customerInfo.email());
        payer.setID(customerInfo.userId().toString());
        payer.setFirstName(customerInfo.firstName());
        payer.setLastName(customerInfo.lastName());
        payment.setPayer(payer);
        var response = (StartPaymentResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        return new StartedPayment(response.getID(), new URI(response.getRedirectURI()));
    }

    @Override
    @SneakyThrows
    public StartedPayment startPayment(
        UUID reservationId, AnonymousCustomerInformation anonymousCustomerInformation, Money amount
    ) {
        var request = new StartPayment();
        var payment = new Payment();
        payment.setAssociatedID(reservationId.toString());
        payment.setAmount(amount.getAmount());
        request.setPayment(payment);
        var payer = new Payer();
        payer.setEmail(anonymousCustomerInformation.email());
        payer.setID(null);
        payer.setFirstName(anonymousCustomerInformation.firstName());
        payer.setLastName(anonymousCustomerInformation.lastName());
        payer.setPhoneNumber(anonymousCustomerInformation.phoneNumber());
        payment.setPayer(payer);
        var response = (StartPaymentResponse) getWebServiceTemplate().marshalSendAndReceive(request);
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
    ExternalPaymentGatewayFacade externalPaymentGatewayFacade(
        Jaxb2Marshaller jaxb2Marshaller, PaymentsConfiguration paymentsConfiguration,
        ReservationRepository reservationRepository
    ) {
        var facade = new ExternalPaymentGatewayFacade(reservationRepository);
        facade.setDefaultUri(paymentsConfiguration.url());
        facade.setMarshaller(jaxb2Marshaller);
        facade.setUnmarshaller(jaxb2Marshaller);
        return facade;
    }

}

@ConfigurationProperties(prefix = "services.payments")
record PaymentsConfiguration(String url) {

}

