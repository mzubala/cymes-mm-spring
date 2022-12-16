package pl.com.bottega.cymes.cinemas.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import pl.com.bottega.cymes.cinemas.dto.RowDto;

import java.util.List;

public record UpdateCinemaHallRequest(@NotEmpty @Valid List<RowDto> layout) {

}
