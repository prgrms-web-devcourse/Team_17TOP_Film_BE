package com.programmers.film.domain.post.repository;

import com.programmers.film.domain.post.domain.PostState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostStateRepository extends JpaRepository<PostState, Long> {

}
