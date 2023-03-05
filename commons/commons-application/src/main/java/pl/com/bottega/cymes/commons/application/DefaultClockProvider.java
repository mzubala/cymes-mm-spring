package pl.com.bottega.cymes.commons.application;

import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.sharedkernel.ClockProvider;

import java.time.Clock;

@Component
public class DefaultClockProvider implements ClockProvider {

    @Override
    public Clock getClock() {
        return Clock.systemUTC();
    }
}
