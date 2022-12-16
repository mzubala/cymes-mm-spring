package pl.com.bottega.cymes.cinemas;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(
    indexes = {
        @Index(columnList = "city, name", unique = true)
    }
)
@NoArgsConstructor
class Cinema {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String city;

    Cinema(String name, String city) {
        this.name = name;
        this.city = city;
    }
}
