package com.programmers.film.domain.user.repository;

import com.programmers.film.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
