package pl.com.bottega.cymes.cinemas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import pl.com.bottega.cymes.cinemas.RowElementKind;

public record RowElementDto(
    @NotNull @Min(0) Integer index,
    @NotNull @Min(1) Integer number,
    @NotNull RowElementKind kind
) {

}
