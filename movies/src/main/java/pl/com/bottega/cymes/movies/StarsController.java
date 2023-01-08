package pl.com.bottega.cymes.movies;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
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

@RestController
@RequestMapping("/stars")
@RequiredArgsConstructor
@Log
@Valid
class StarsController {

    private final StarRepository starRepository;

    @PostMapping
    void create(@RequestBody @Valid CreateStarRequest request) {
        starRepository.save(new Star(null, request.firstName(), request.middleName(), request.lastName()));
    }

    @PutMapping("/{id}")
    @Transactional
    void update(@PathVariable Long id, @RequestBody @Valid UpdateStarRequest request) {
        var star = starRepository.getReferenceById(id);
        star.setFirstName(request.firstName());
        star.setLastName(request.lastName());
        star.setMiddleName(request.middleName());
    }

    @GetMapping
    Page<StarDto> search(@RequestParam String phrase, Pageable pagination) {
        var paginationWithSort = PageRequest.of(
            pagination.getPageNumber(),
            pagination.getPageSize(),
            pagination.getSortOr(Sort.by(Star_.LAST_NAME))
        );
        return starRepository.findAll(StarSpecifications.byPhrase(phrase), paginationWithSort)
            .map(Star::toDto);
    }
}
