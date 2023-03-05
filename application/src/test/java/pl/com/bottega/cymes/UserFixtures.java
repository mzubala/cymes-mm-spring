package pl.com.bottega.cymes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.users.requests.RegisterUserAccountRequest;
import pl.com.bottega.cymes.users.requests.RegisterUserAccountResponse;

@Component
@RequiredArgsConstructor
class UserFixtures {

    private final UsersApi usersApi;
    Long customerId, adminId;

    RegisterUserAccountRequest admin = new RegisterUserAccountRequest(
        "admin@admin.com", "Test9000", "Jane", "Doe"
    );

    RegisterUserAccountRequest customer = new RegisterUserAccountRequest(
        "john@test.com", "Test123456", "John", "Doe"
    );


    void create() {
        adminId = usersApi.registerFirstAdmin(admin).getObject(RegisterUserAccountResponse.class).userId();
        customerId = usersApi.registerCustomer(customer).getObject(RegisterUserAccountResponse.class).userId();
    }

    void loginAsAdmin() {
        usersApi.logIn(admin.email(), admin.password());
    }

    void loginAsCustomer() {
        usersApi.logIn(customer.email(), customer.password());
    }

}
