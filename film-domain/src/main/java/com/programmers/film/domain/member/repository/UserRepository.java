package com.programmers.film.domain.member.repository;

import com.programmers.film.domain.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
