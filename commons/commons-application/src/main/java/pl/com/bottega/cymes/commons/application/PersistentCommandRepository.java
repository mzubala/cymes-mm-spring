package pl.com.bottega.cymes.commons.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface PersistentCommandRepository extends JpaRepository<PersistentCommand, Long> {
    List<PersistentCommand> findAll();
}
