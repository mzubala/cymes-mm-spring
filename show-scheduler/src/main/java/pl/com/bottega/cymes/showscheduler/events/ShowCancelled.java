package pl.com.bottega.cymes.showscheduler.events;

import pl.com.bottega.cymes.sharedkernel.Event;

public record ShowCancelled(Long showId) implements Event {
}
