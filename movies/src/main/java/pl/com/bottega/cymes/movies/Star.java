package pl.com.bottega.cymes.movies;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.com.bottega.cymes.movies.dto.StarDto;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;

    StarDto toDto() {
        return new StarDto(id, firstName, middleName, lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Star))
            return false;

        Star other = (Star) o;

        return id != null &&
            id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
