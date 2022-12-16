package pl.com.bottega.cymes.cinemas.dto;

import java.util.List;

public record DetailedCinemaHallInfoDto(Long id, String name, Integer capacity, List<RowDto> rows, Boolean suspended) {
}
