package pl.com.bottega.cymes.cinemas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RowDto(
    @NotNull Integer number,
    @Valid @NotEmpty List<RowElementDto> elements
) {
}
