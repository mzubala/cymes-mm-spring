package pl.com.bottega.cymes.cinemas.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import pl.com.bottega.cymes.cinemas.dto.RowDto;

import java.util.List;

public record CreateCinemaHallRequest(
    @NotNull Long cinemaId, @NotBlank String name, @NotEmpty @Valid List<RowDto> layout
) {
}
