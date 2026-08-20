package com.FieldService.Security;

import com.FieldService.Entity.UserAuth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

	private final SecretKey key;
	private final long validTokenTime;

	public JWTUtil(
			@Value("${app.jwt.secret}") String secret,
			@Value("${app.jwt.expiration-ms}") long expirationMs) {

		this.key = Keys.hmacShaKeyFor(
				secret.getBytes(StandardCharsets.UTF_8)
		);

		this.validTokenTime = expirationMs;
	}

	public String generateToken(UserAuth user) {

		Map<String, Object> claims = new HashMap<>();

		claims.put(
				"role",
				user.getRole().name()
		);

		Date now = new Date();

		Date expire =
				new Date(now.getTime() + validTokenTime);

		return Jwts.builder()
				.claims(claims)
				.subject(user.getUserEmail())
				.issuedAt(now)
				.expiration(expire)
				.signWith(key)
				.compact();
	}

	public boolean validateToken(String token) {

		try {

			Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token);

			return true;

		} catch (Exception e) {

			return false;
		}
	}

	public Claims getClaims(String token) {

		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public String getUserEmail(String token) {

		return getClaims(token).getSubject();
	}

	public String extractToken(String header) {

		if (header != null
				&& header.startsWith("Bearer ")) {

			return header.substring(7);
		}

		return null;
	}
}