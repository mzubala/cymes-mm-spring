package pl.com.bottega.cymes.cinemas;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor
class Row {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Integer number;

    @OneToMany(cascade = ALL)
    @JoinColumn
    private List<RowElement> elements;

    Row(Integer number, List<RowElement> elements) {
        this.number = number;
        this.elements = elements;
    }

    int capacity() {
        return elements.stream().mapToInt(RowElement::capacity).sum();
    }
}
