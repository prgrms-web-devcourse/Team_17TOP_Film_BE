package com.programmers.film.domain.dummy.repository;

import com.programmers.film.domain.dummy.domain.DummyEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DummyRepository extends JpaRepository<DummyEntity, Long> {

  Optional<DummyEntity> findById(Long id);
}
