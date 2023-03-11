package pl.com.bottega.cymes.users;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.bottega.cymes.users.dto.UserDto;

import java.util.Optional;

interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    <T> Optional<T> findById(Long id, Class<T> projection);
}
