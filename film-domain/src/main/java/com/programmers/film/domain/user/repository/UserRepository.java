package com.programmers.film.domain.user.repository;

import com.programmers.film.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select u from User u join fetch u.group g left join fetch g.permissions gp join fetch gp.permission where u.username = :username")
	Optional<User> findByUsername(String username);

	@Query("select u from User u join fetch u.group g left join fetch g.permissions gp join fetch gp.permission where u.provider = :provider and u.providerId = :providerId")
	Optional<User> findByProviderAndProviderId(String provider, String providerId);

}