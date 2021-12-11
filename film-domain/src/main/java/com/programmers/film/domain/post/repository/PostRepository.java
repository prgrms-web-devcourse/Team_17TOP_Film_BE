package com.programmers.film.domain.post.repository;

import com.programmers.film.domain.post.domain.Post;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByAvailableAtBetween (LocalDate start, LocalDate end);
}
