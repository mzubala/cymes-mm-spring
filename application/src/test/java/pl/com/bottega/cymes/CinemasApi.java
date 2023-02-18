package pl.com.bottega.cymes;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.com.bottega.cymes.cinemas.dto.BasicCinemaInfoDto;
import pl.com.bottega.cymes.cinemas.dto.DetailedCinemaInfoDto;
import pl.com.bottega.cymes.cinemas.requests.CreateCinemaHallRequest;
import pl.com.bottega.cymes.cinemas.requests.CreateCinemaRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
class CinemasApi extends Api {

    private List<BasicCinemaInfoDto> cinemas;

    private final Map<Long, DetailedCinemaInfoDto> detailedCinemaInfoDtoMap = new HashMap<>();

    CinemasApi(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    @SneakyThrows
    ResultActions create(CreateCinemaRequest request) {
        return post("/cinemas", request);
    }

    @SneakyThrows
    ResultActions createCinemaHall(CreateCinemaHallRequest request) {
        return post("/halls", request);
    }

    @SneakyThrows
    List<BasicCinemaInfoDto> getCinemas() {
        if (cinemas == null) {
            cinemas = getList("/cinemas", BasicCinemaInfoDto.class);
        }
        return cinemas;
    }

    Long getCinemaId(String city, String name) {
        return getCinemas().stream().filter(c -> c.city().equals(city) && c.name().equals(name))
            .findFirst().orElseThrow(IllegalArgumentException::new).id();
    }

    DetailedCinemaInfoDto getCinemaDetails(Long cinemaId) {
        return detailedCinemaInfoDtoMap.computeIfAbsent(
            cinemaId,
            (id) -> getObject("/cinemas/{id}?at={at}", DetailedCinemaInfoDto.class, cinemaId.toString(), Instant.now())
        );
    }
}

