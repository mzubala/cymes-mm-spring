package pl.com.bottega.cymes.commons.test;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TimeFixtures {

    public Instant tomorrowAt(int hour, int minute) {
        var tomorrowSameTime = LocalDateTime.now().plusDays(1);
        return LocalDateTime.of(
                tomorrowSameTime.getYear(), tomorrowSameTime.getMonth(), tomorrowSameTime.getDayOfMonth(), hour, minute)
            .atZone(ZoneId.systemDefault()).toInstant();
    }

    public LocalDate tomorrow() {
        return LocalDate.now().plusDays(1);
    }

}
