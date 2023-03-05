package pl.com.bottega.cymes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import pl.com.bottega.cymes.users.requests.RegisterUserAccountRequest;

@Component
class UsersApi extends Api {

    public UsersApi(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    ExtendedResultActions registerCustomer(RegisterUserAccountRequest request) {
        return post("/registration/customer", request);
    }

    ExtendedResultActions registerFirstAdmin(RegisterUserAccountRequest request) {
        return post("/registration/first-admin", request);
    }
}
