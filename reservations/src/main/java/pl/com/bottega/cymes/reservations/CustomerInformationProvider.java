package pl.com.bottega.cymes.reservations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.users.UsersFacade;

interface CustomerInformationProvider {
    CustomerInformation getByUserId(Long userId);
}

@Component
@RequiredArgsConstructor
class UserModuleCustomerInformationProvider implements CustomerInformationProvider {

    private final UsersFacade usersFacade;

    @Override
    public CustomerInformation getByUserId(Long userId) {
        var user = usersFacade.getUser(userId);
        return new CustomerInformation(
            userId, user.firstName(), user.lastName(), null, user.email()
        );
    }
}