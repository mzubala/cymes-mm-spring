package pl.com.bottega.cymes.sharedkernel;

import java.time.Clock;
import java.time.Instant;

public interface ClockProvider {
    Clock getClock();

    default Instant now() {
        return getClock().instant();
    }
}
