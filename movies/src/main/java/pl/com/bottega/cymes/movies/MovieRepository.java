package pl.com.bottega.cymes.movies;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

import static pl.com.bottega.cymes.movies.Movie_.ACTORS;
import static pl.com.bottega.cymes.movies.Movie_.DIRECTOR;
import static pl.com.bottega.cymes.movies.Movie_.GENRES;

interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    @EntityGraph(attributePaths = {ACTORS, GENRES, DIRECTOR}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Movie> findAll(@Nullable Specification<Movie> spec, Pageable pageable);

    <T> Optional<T> findById(Long movieId, Class<T> projection);

    <T> List<T> findByIdIn(Iterable<Long> ids, Class<T> projection);
}
