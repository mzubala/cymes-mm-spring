package pl.com.bottega.cymes.commons.application;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
class PersistentCommand {
    private Long id;
    private Long userId;
    private String jsonPayload;
    private String payloadClass;
    private Instant executedAt;

    PersistentCommand(Long userId, String jsonPayload, String payloadClass, Instant executedAt) {
        this.userId = userId;
        this.jsonPayload = jsonPayload;
        this.payloadClass = payloadClass;
        this.executedAt = executedAt;
    }
}
