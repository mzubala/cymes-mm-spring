package pl.com.bottega.cymes.commons.application;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@Entity
class PersistentCommand {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long userId;
    @Column(length = 4096)
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
