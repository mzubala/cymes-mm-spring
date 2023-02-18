package pl.com.bottega.cymes.showscheduler;

import org.springframework.data.jpa.repository.JpaRepository;

interface ShowRepository extends JpaRepository<Show, Long> {
}
