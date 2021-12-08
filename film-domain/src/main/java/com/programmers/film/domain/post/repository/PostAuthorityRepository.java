package com.programmers.film.domain.post.repository;

import com.programmers.film.domain.post.domain.PostAuthority;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostAuthorityRepository extends JpaRepository<PostAuthority, Long> {
    List<PostAuthority> findByUserId (Long userId);
    List<PostAuthority> findByPostId (Long postId);
}
