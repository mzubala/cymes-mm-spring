package pl.com.bottega.cymes.cinemas;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(
    indexes = {
        @Index(unique = true, columnList = "name, cinema_id")
    }
)
@NoArgsConstructor
class CinemaHall {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private Integer capacity;

    @OneToMany(cascade = ALL)
    @JoinColumn
    private List<Row> rows;

    @ManyToOne
    private Cinema cinema;

    public CinemaHall(Cinema cinema, String name, List<Row> rows) {
        this.cinema = cinema;
        this.name = name;
        setRows(rows);
    }

    void setRows(List<Row> rows) {
        this.rows = rows;
        this.capacity = calculateCapacity();
    }

    private int calculateCapacity() {
        return rows.stream().mapToInt(Row::capacity).sum();
    }
}
