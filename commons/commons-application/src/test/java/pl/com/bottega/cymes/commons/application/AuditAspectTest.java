package pl.com.bottega.cymes.commons.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.commons.test.Faker;
import pl.com.bottega.cymes.commons.test.FixedClockProvider;
import pl.com.bottega.cymes.commons.test.IntegrationTest;
import pl.com.bottega.cymes.sharedkernel.Command;
import pl.com.bottega.cymes.sharedkernel.UserCommand;

import static org.assertj.core.api.Assertions.assertThat;


@IntegrationTest
@SpringBootApplication
class AuditAspectTest {

    @Autowired
    private PersistentCommandRepository persistentCommandRepository;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FixedClockProvider clockProvider;

    @Autowired
    private GloballyAuditedBean globallyAuditedBean;


    @Autowired
    private LocallyAuditedBean locallyAuditedBean;

    @Test
    void savesAuditAfterSuccessfulCommandExecution() {
        // given
        var cmd1 = new TestCommand(faker.number().numberBetween(0, 1000));
        var cmd2 = new OtherTestCommand(faker.shakespeare().asYouLikeItQuote());

        // when
        globallyAuditedBean.doWork(cmd1);
        globallyAuditedBean.doOtherWork(cmd2);

        // then
        assertCommandPersisted(cmd1);
        assertCommandPersisted(cmd2);
    }

    @SneakyThrows
    private void assertCommandPersisted(UserCommand cmd) {
        assertThat(persistentCommandRepository.findAll()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(
            new PersistentCommand(
                1L,
                cmd.getUserId(),
                objectMapper.writeValueAsString(cmd),
                cmd.getClass().getName(),
                clockProvider.now()
            )
        );
    }

    @Component
    @Audited
    @Transactional
    static class GloballyAuditedBean {

        void doWork(TestCommand command) {

        }

        void doOtherWork(OtherTestCommand command) {

        }

        @Transactional(readOnly = true)
        String getSome() {
            return "text";
        }

    }

    @Component
    @Transactional
    static class LocallyAuditedBean {

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    static class TestCommand extends UserCommand {
        Integer number;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    static class OtherTestCommand extends UserCommand {
        String text;
    }

    static class NonUserCommand implements Command {

    }

}
