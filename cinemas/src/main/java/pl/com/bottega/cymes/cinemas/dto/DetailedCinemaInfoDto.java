package pl.com.bottega.cymes.cinemas.dto;

import java.util.List;

public record DetailedCinemaInfoDto(
    Long id, String name, String city, List<BasicCinemaHallInfoDto> halls, Boolean suspended
) {
    public BasicCinemaHallInfoDto getHall(String name) {
        return halls.stream().filter(hall -> hall.name().equals(name)).findFirst().orElseThrow(
            IllegalArgumentException::new);
    }
}
