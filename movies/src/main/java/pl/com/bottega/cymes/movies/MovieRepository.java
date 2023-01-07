package pl.com.bottega.cymes.movies;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import static pl.com.bottega.cymes.movies.Movie_.ACTORS;
import static pl.com.bottega.cymes.movies.Movie_.DIRECTOR;
import static pl.com.bottega.cymes.movies.Movie_.GENRES;

interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    @EntityGraph(attributePaths = {ACTORS, GENRES, DIRECTOR})
    Page<Movie> findAll(@Nullable Specification<Movie> spec, Pageable pageable);
}
