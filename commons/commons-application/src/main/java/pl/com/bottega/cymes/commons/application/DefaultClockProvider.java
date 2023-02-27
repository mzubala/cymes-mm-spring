package pl.com.bottega.cymes.commons.application;

import jakarta.validation.ClockProvider;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class DefaultClockProvider implements ClockProvider {

    @Override
    public Clock getClock() {
        return Clock.systemUTC();
    }
}
