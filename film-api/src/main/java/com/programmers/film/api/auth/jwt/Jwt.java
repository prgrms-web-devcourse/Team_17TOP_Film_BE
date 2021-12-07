package com.programmers.film.api.auth.jwt;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Arrays;
import java.util.Date;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
public final class Jwt {

	private final String issuer;

	private final String clientSecret;

	private final int expirySeconds;

	private final Algorithm algorithm;

	private final JWTVerifier jwtVerifier;

	public Jwt(String issuer, String clientSecret, int expirySeconds) {
		this.issuer = issuer;
		this.clientSecret = clientSecret;
		this.expirySeconds = expirySeconds;
		this.algorithm = Algorithm.HMAC512(clientSecret);
		this.jwtVerifier = com.auth0.jwt.JWT.require(algorithm)
			.withIssuer(issuer)
			.build();
	}

	public String sign(Claims claims) {
		Date now = new Date();
		JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
		builder.withIssuer(issuer);
		builder.withIssuedAt(now);
		if (expirySeconds > 0) {
			builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1000L));
		}
		builder.withArrayClaim("roles", claims.roles);
		builder.withClaim("provider", claims.provider);
		builder.withClaim("provider_id", claims.providerId);
		return builder.sign(algorithm);
	}

	public Claims verify(String token) throws JWTVerificationException {
		return new Claims(jwtVerifier.verify(token));
	}

	static public class Claims {

		Date iat;
		Date exp;
		String[] roles;
		String provider;        // ex) kakao
		String providerId;        // ex) 2023353092

		private Claims() {/*no-op*/}

		Claims(DecodedJWT decodedJWT) {
			this.iat = decodedJWT.getIssuedAt();
			this.exp = decodedJWT.getExpiresAt();

			Claim roles = decodedJWT.getClaim("roles");
			if (!roles.isNull()) {
				this.roles = roles.asArray(String.class);
			}

			Claim provider = decodedJWT.getClaim("provider");
			if (!provider.isNull()) {
				this.provider = provider.asString();
			}

			Claim providerId = decodedJWT.getClaim("provider_id");
			if (!providerId.isNull()) {
				this.providerId = providerId.asString();
			}
		}

		public static Claims from(String[] roles, String provider, String providerId) {
			Claims claims = new Claims();
			claims.roles = roles;
			claims.provider = provider;
			claims.providerId = providerId;
			return claims;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("roles", Arrays.toString(roles))
				.append("provider", provider)
				.append("providerId", providerId)
				.append("iat", iat)
				.append("exp", exp)
				.toString();
		}
	}

}
