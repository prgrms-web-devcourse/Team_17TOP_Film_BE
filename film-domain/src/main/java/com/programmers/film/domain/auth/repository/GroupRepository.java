package com.programmers.film.domain.auth.repository;

import com.programmers.film.domain.auth.domain.Group;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

	Optional<Group> findByName(String name);
}