package pl.com.bottega.cymes.commons.application;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.sharedkernel.UserCommand;

@Aspect
@Component
@Order(0)
class InjectUserIdAspect {

    @Before(value = "execution(* *(@InjectUserId (*))) && args(userCommand)", argNames = "userCommand")
    void injectUserId(UserCommand userCommand) {
        if (userCommand.getUserId() != null) {
            return;
        }
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            var userId = (Long) authentication.getPrincipal();
            userCommand.setUserId(userId);
        }
    }

}
