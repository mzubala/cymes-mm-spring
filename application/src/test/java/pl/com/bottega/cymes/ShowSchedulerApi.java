package pl.com.bottega.cymes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.com.bottega.cymes.showscheduler.dto.ShowsGroupDto;
import pl.com.bottega.cymes.showscheduler.requests.ScheduleShowRequest;

import java.time.LocalDate;
import java.util.List;

@Component
class ShowSchedulerApi extends Api {

    ShowSchedulerApi(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    ResultActions schedule(ScheduleShowRequest request) {
        return post("/shows", request);
    }

    List<ShowsGroupDto> getShows(Long cinemaId, LocalDate when) {
        return getList("/shows?cinemaId={cinemaId}&when={when}", ShowsGroupDto.class, cinemaId, when);
    }
}
