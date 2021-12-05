package com.programmers.film.domain.auth.repository;

import com.programmers.film.domain.auth.domain.Auth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthRepository extends JpaRepository<Auth, Long> {

	@Query("select u from Auth u join fetch u.group g left join fetch g.permissions gp join fetch gp.permission where u.username = :username")
	Optional<Auth> findByUsername(String username);

	@Query("select u from Auth u join fetch u.group g left join fetch g.permissions gp join fetch gp.permission where u.provider = :provider and u.providerId = :providerId")
	Optional<Auth> findByProviderAndProviderId(String provider, String providerId);

}