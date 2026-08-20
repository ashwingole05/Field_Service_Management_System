package com.FieldService.Repository;

import com.FieldService.Entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserAuth, Long> {

	Optional<UserAuth> findByUserEmail(String userEmail);

	Optional<UserAuth> findByResetToken(String resetToken);

	boolean existsByUserEmail(String userEmail);
}