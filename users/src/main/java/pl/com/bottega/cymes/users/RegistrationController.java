package pl.com.bottega.cymes.users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.com.bottega.cymes.users.requests.RegisterUserAccountRequest;
import pl.com.bottega.cymes.users.requests.RegisterUserAccountResponse;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/customer")
    RegisterUserAccountResponse registerCustomer(@Valid @RequestBody RegisterUserAccountRequest request) {
        return createUser(request, UserRole.CUSTOMER);
    }

    @PostMapping("/first-admin")
    RegisterUserAccountResponse registerFirstAdmin(@Valid @RequestBody RegisterUserAccountRequest request) {
        if(userRepository.count() == 0) {
            return createUser(request, UserRole.ADMIN);
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "It's only possible to create an admin user with this endpoint when there have been no other users created yet.");
        }
    }

    private RegisterUserAccountResponse createUser(RegisterUserAccountRequest request, UserRole role) {
        var user = new User(request.email(), passwordEncoder.encode(request.password()), request.firstName(), request.lastName());
        user.addRole(role);
        userRepository.save(user);
        return new RegisterUserAccountResponse(user.getId());
    }

}
