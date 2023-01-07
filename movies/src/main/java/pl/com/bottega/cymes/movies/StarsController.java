package pl.com.bottega.cymes.movies;

import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.movies.dto.StarDto;
import pl.com.bottega.cymes.movies.requests.CreateStarRequest;
import pl.com.bottega.cymes.movies.requests.UpdateStarRequest;

import java.util.List;

@RestController
@RequestMapping("/stars")
@Log
class StarsController {

    @PostMapping
    void create(@RequestBody CreateStarRequest request) {
        log.info(String.format("Create star %s", request));
    }

    @PutMapping("/{id}")
    void update(@PathVariable Long id, @RequestBody UpdateStarRequest request) {
        log.info(String.format("Update star %d %s", id, request));
    }

    @GetMapping
    Page<StarDto> search(@RequestParam String phrase, Pageable pagination) {
        return new PageImpl(List.of(new StarDto(1L, "Johnny", null, "Depp")), pagination, 1);
    }
}
