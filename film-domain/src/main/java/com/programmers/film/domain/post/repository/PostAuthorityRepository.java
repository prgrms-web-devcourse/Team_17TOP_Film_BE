package com.programmers.film.domain.post.repository;

import com.programmers.film.domain.post.domain.PostAuthority;
import com.programmers.film.domain.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostAuthorityRepository extends JpaRepository<PostAuthority, Long> {
    List<PostAuthority> findByUserId (Long userId);
    List<PostAuthority> findByPostId (Long postId);

    @Query("select u from PostAuthority a join a.post p join a.user u WHERE a.post.id=:postId")
    List<User> findUserByPostId(@Param("postId") Long postId);

    @Query("select a from PostAuthority a "
        + "join a.post p "
        + "join a.user u "
        + "where u.id=:userId and p.id=:postId")
    Optional<PostAuthority> findByUserIdAndPostId(Long userId, Long postId);
}
