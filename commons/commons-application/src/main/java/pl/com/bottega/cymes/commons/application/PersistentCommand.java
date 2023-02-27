package pl.com.bottega.cymes.commons.application;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
class PersistentCommand {
    private Long id;
    private Long userId;
    private String jsonPayload;
    private String payloadClass;
    private Instant executedAt;
}
