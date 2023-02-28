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
import java.util.List;

@Component
class CinemasApi extends Api {

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
        return getList("/cinemas", BasicCinemaInfoDto.class);
    }

    Long getCinemaId(String city, String name) {
        return getCinemas().stream().filter(c -> c.city().equals(city) && c.name().equals(name))
            .findFirst().orElseThrow(IllegalArgumentException::new).id();
    }

    DetailedCinemaInfoDto getCinemaDetails(Long cinemaId) {
        return getObject("/cinemas/{id}?at={at}", DetailedCinemaInfoDto.class, cinemaId.toString(), Instant.now());
    }
}

