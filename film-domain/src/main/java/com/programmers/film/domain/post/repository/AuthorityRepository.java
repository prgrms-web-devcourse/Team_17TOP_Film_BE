package com.programmers.film.domain.post.repository;

import com.programmers.film.domain.post.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
