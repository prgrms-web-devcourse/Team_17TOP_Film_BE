package com.programmers.film.domain.user.repository;

import com.programmers.film.domain.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    @Query(value = "SELECT u FROM User u WHERE u.nickname LIKE CONCAT(:nickname, '%') AND u.nickname > :lastNickname")
    Page<User> findByNicknameStartsWithAndNicknameGreaterThan(String nickname, String lastNickname, Pageable pageable);
}
