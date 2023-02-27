package pl.com.bottega.cymes.commons.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.commons.test.Faker;
import pl.com.bottega.cymes.commons.test.FixedClockProvider;
import pl.com.bottega.cymes.commons.test.IntegrationTest;
import pl.com.bottega.cymes.sharedkernel.Command;
import pl.com.bottega.cymes.sharedkernel.UserCommand;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
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

    @Autowired
    private NonTxBean nonTxBean;

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

    @Test
    void doesNotSaveCommandWhenOtherTypesOfMethodsAreCalled() {
        // when
        globallyAuditedBean.getSome();
        globallyAuditedBean.doNonUserWork(new NonUserCommand());

        // then
        assertNoCommandsPersisted();
    }

    @Test
    void savesCommandWhenNonTransactionalMethodIsCalled() {
        // given
        var cmd = new TestCommand();

        // when
        nonTxBean.doWork(cmd);

        // then
        assertCommandPersisted(cmd);
    }

    @Test
    void savesCommandsPassedToLocallyAuditedMethods() {
        // given
        var cmd = new TestCommand();

        // when
        locallyAuditedBean.doWork(cmd);

        // then
        assertCommandPersisted(cmd);
    }

    @Test
    void doesNotSaveCommandIfAuditedMethodFals() {
        // given
        var cmd = new TestCommand();

        // when
        try {
            globallyAuditedBean.doFailingWork(cmd);
        } catch (Exception ignored) {

        }


        // then
        assertNoCommandsPersisted();
    }

    @SneakyThrows
    private void assertCommandPersisted(UserCommand cmd) {
        assertThat(persistentCommandRepository.findAll()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(
            new PersistentCommand(
                cmd.getUserId(),
                objectMapper.writeValueAsString(cmd),
                cmd.getClass().getName(),
                clockProvider.now()
            )
        );
    }

    private void assertNoCommandsPersisted() {
        assertThat(persistentCommandRepository.count()).isEqualTo(0L);
    }
}

@Component
@Audited
@Transactional
class GloballyAuditedBean {

    void doWork(TestCommand command) {

    }

    void doOtherWork(OtherTestCommand command) {

    }

    @Transactional(readOnly = true)
    String getSome() {
        return "text";
    }

    void doNonUserWork(NonUserCommand nonUserCommand) {

    }

    void doFailingWork(TestCommand cmd) {
        throw new RuntimeException("Error!");
    }
}

@Component
@Audited
class NonTxBean {

    void doWork(TestCommand command) {

    }
}


@Component
@Transactional
class LocallyAuditedBean {

    @Audited
    void doWork(TestCommand command) {

    }
}

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
class TestCommand extends UserCommand {
    Integer number;
}

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
class OtherTestCommand extends UserCommand {
    String text;
}

class NonUserCommand implements Command {

}
