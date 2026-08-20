package com.FieldService.Security;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlockService {

	private final Set<String> blockedTokens =
			ConcurrentHashMap.newKeySet();

	public void blockToken(String token) {
		blockedTokens.add(token);
	}

	public boolean isTokenBlocked(String token) {
		return blockedTokens.contains(token);
	}
}