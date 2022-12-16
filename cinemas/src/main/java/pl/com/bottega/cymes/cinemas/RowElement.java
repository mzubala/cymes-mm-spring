package pl.com.bottega.cymes.cinemas;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor
class RowElement {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Integer index;

    private Integer number;

    @Enumerated(STRING)
    private RowElementKind elementKind;

    RowElement(Integer number, RowElementKind elementKind) {
        this.number = number;
        this.elementKind = elementKind;
    }

    int capacity() {
        if (getElementKind() == RowElementKind.SEAT) {
            return 1;
        } else {
            return 0;
        }
    }
}
