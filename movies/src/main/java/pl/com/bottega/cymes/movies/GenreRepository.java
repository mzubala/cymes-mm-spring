package pl.com.bottega.cymes.movies;

import org.springframework.data.jpa.repository.JpaRepository;

interface GenreRepository extends JpaRepository<Genre, Long> {
}
