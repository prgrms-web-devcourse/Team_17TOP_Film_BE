package com.programmers.film.domain.post.repository;

import com.programmers.film.domain.member.domain.User;
import com.programmers.film.domain.post.domain.PostAuthority;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostAuthorityRepository extends JpaRepository<PostAuthority, Long> {
    Optional<PostAuthority> findByUserId (Long userId);
    Optional<PostAuthority> findByPostId (Long postId);
}
