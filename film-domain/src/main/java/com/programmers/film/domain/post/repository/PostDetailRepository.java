package com.programmers.film.domain.post.repository;

import com.programmers.film.domain.post.domain.PostDetail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDetailRepository extends JpaRepository<PostDetail, Long> {
    Optional<PostDetail> findByPostId(Long postId);
}
