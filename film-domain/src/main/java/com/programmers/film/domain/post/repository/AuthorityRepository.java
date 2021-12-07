package com.programmers.film.domain.post.repository;

import com.programmers.film.domain.post.domain.PostAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<PostAuthority, Long> {

}
