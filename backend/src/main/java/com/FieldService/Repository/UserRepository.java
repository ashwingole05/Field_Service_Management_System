package com.FieldService.Repository;

import com.FieldService.Entity.UserAuth;
import com.FieldService.ENUM.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.Collection;

@Repository
public interface UserRepository
		extends JpaRepository<UserAuth, Long> {

	Optional<UserAuth> findByUserEmail(
			String userEmail
	);

	Optional<UserAuth> findByResetToken(
			String resetToken
	);

	List<UserAuth> findByRole(Role role);

	List<UserAuth> findByRoleIn(Collection<Role> roles);

	boolean existsByRole(Role role);

	boolean existsByUserEmail(
			String userEmail
	);
}
