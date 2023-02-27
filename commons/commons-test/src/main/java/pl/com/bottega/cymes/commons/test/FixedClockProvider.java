package pl.com.bottega.cymes.commons.test;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.sharedkernel.ClockProvider;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Primary
@Component
public class FixedClockProvider implements ClockProvider {

    private Clock clock;

    public FixedClockProvider() {
        fixAt(Instant.now());
    }

    @Override
    public Clock getClock() {
        return clock;
    }

    public void fixAt(Instant time) {
        clock = Clock.fixed(time, ZoneId.systemDefault());
    }
}
