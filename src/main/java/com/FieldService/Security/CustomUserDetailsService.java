package com.FieldService.Security;

import com.FieldService.ENUM.Permissions;
import com.FieldService.Entity.UserAuth;
import com.FieldService.Repository.UserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepo;

	public CustomUserDetailsService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String userEmail) {

		UserAuth user = userRepo.findByUserEmail(userEmail)
				.orElseThrow(() ->
						new RuntimeException("User not found")
				);

		Set<Permissions> permissions =
				RoleBasedPermission
						.getRoleWisePermissions()
						.getOrDefault(
								user.getRole(),
								Collections.emptySet()
						);

		Set<GrantedAuthority> authorities =
				permissions.stream()
						.map(permission ->
								new SimpleGrantedAuthority(
										permission.name()
								)
						)
						.collect(Collectors.toSet());

		return new org.springframework.security.core.userdetails.User(
				user.getUserEmail(),
				user.getPassword(),
				authorities
		);
	}
}