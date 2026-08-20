package com.FieldService.Service;

import com.FieldService.DTO.AuthResponseDTO;
import com.FieldService.DTO.LoginRequestDTO;
import com.FieldService.DTO.RegisterRequestDTO;
import com.FieldService.Entity.UserAuth;
import com.FieldService.ENUM.Role;
import com.FieldService.Repository.UserRepository;
import com.FieldService.Security.EmailLogService;
import com.FieldService.Security.JWTUtil;
import com.FieldService.Security.TokenBlockService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserAuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JWTUtil jwtUtil;
	private final EmailLogService emailLogService;
	private final TokenBlockService tokenBlockService;

	public UserAuthService(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			JWTUtil jwtUtil,
			EmailLogService emailLogService,
			TokenBlockService tokenBlockService
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.emailLogService = emailLogService;
		this.tokenBlockService = tokenBlockService;
	}

	// REGISTER
	public String register(RegisterRequestDTO request) {

		if (userRepository
				.findByUserEmail(request.getUserEmail())
				.isPresent()) {

			throw new RuntimeException(
					"Email already registered"
			);
		}

		Role role = request.getRole();

		if (role == null) {
			role = Role.CUSTOMER;
		}

		UserAuth user = UserAuth.builder()
				.userName(request.getUserName())
				.userEmail(request.getUserEmail())
				.phone(request.getPhone())
				.password(
						passwordEncoder.encode(
								request.getPassword()
						)
				)
				.role(role)
				.build();

		userRepository.save(user);

		return "User registered successfully";
	}

	// LOGIN
	public AuthResponseDTO login(
			LoginRequestDTO request) {

		UserAuth user =
				userRepository
						.findByUserEmail(
								request.getUserEmail()
						)
						.orElseThrow(() ->
								new RuntimeException(
										"Invalid email or password"
								)
						);

		if (!passwordEncoder.matches(
				request.getPassword(),
				user.getPassword()
		)) {

			throw new RuntimeException(
					"Invalid email or password"
			);
		}

		String token =
				jwtUtil.generateToken(user);

		return new AuthResponseDTO(
				token,
				"Login successful"
		);
	}

	// LOGOUT
	public void logout(String token) {

		if (token == null || token.isBlank()) {
			throw new RuntimeException(
					"Token is required"
			);
		}

		tokenBlockService.blockToken(token);
	}

	// FORGOT PASSWORD
	public void forgetPassword(String userEmail) {

		UserAuth user =
				userRepository
						.findByUserEmail(userEmail)
						.orElseThrow(() ->
								new RuntimeException(
										"User not found"
								)
						);

		String token =
				UUID.randomUUID().toString();

		user.setResetToken(token);

		user.setResetTokenExpiry(
				new Date(
						System.currentTimeMillis()
								+ 15 * 60 * 1000
				)
		);

		userRepository.save(user);

		emailLogService.sendResetPasswordEmail(
				user.getUserEmail(),
				token
		);
	}

	// RESET PASSWORD
	public void resetPassword(
			String token,
			String newPassword) {

		UserAuth user =
				userRepository
						.findByResetToken(token)
						.orElseThrow(() ->
								new RuntimeException(
										"Invalid reset token"
								)
						);

		if (user.getResetTokenExpiry() == null
				|| user.getResetTokenExpiry()
				.before(new Date())) {

			throw new RuntimeException(
					"Reset token has expired"
			);
		}

		if (newPassword == null
				|| newPassword.isBlank()) {

			throw new RuntimeException(
					"New password cannot be empty"
			);
		}

		user.setPassword(
				passwordEncoder.encode(
						newPassword
				)
		);

		user.setResetToken(null);
		user.setResetTokenExpiry(null);

		userRepository.save(user);
	}
}