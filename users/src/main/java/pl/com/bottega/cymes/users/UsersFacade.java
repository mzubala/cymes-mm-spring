package pl.com.bottega.cymes.users;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.users.dto.UserDto;

@Component
@RequiredArgsConstructor
public class UsersFacade {

    private final UserRepository userRepository;

    public UserDto getUser(Long id) {
        return userRepository.findById(id, UserDto.class)
            .orElseThrow(() -> new EntityNotFoundException("No such user " + id));
    }

}
