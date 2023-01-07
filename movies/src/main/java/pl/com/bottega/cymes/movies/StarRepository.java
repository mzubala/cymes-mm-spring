package pl.com.bottega.cymes.movies;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface StarRepository extends JpaRepository<Star, Long>, JpaSpecificationExecutor<Star> {
}
