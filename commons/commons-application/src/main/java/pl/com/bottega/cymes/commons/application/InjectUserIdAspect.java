package pl.com.bottega.cymes.commons.application;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.sharedkernel.UserCommand;

@Component
@Aspect
// TODO - set order
class InjectUserIdAspect {

    // TODO
    void injectUserId(UserCommand userCommand) {
        // TODO
    }

}
