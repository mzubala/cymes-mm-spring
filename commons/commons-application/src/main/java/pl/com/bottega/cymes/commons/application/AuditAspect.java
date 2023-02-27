package pl.com.bottega.cymes.commons.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.sharedkernel.ClockProvider;
import pl.com.bottega.cymes.sharedkernel.UserCommand;

@Aspect
@Component
@RequiredArgsConstructor
class AuditAspect {

    private final PersistentCommandRepository persistentCommandRepository;
    private final ObjectMapper objectMapper;
    private final ClockProvider clockProvider;

    @AfterReturning(value = "(@within(Audited) || @annotation(Audited)) && args(userCommand)")
    @SneakyThrows
    void audit(UserCommand userCommand) {
        persistentCommandRepository.save(
            new PersistentCommand(
                userCommand.getUserId(),
                objectMapper.writeValueAsString(userCommand),
                userCommand.getClass().getName(),
                clockProvider.now()
            )
        );
    }

}
