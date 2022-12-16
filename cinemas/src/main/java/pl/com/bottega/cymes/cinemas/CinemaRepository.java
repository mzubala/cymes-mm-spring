package pl.com.bottega.cymes.cinemas;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface CinemaRepository extends JpaRepository<Cinema, Long> {
    <T> List<T> findByOrderByNameAscCityAsc(Class<T> projection);
}
