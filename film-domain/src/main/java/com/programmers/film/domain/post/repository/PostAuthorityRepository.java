package com.programmers.film.domain.post.repository;

import com.programmers.film.domain.post.domain.PostAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import com.programmers.film.domain.member.domain.User;
import java.util.Optional;

public interface PostAuthorityRepository extends JpaRepository<PostAuthority, Long> {
    Optional<PostAuthority> findByUserId (Long userId);
    Optional<PostAuthority> findByPostId (Long postId);
}
