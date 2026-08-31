package com.FieldService.Service;

import com.FieldService.DTO.AuthResponseDTO;
import com.FieldService.DTO.CurrentUserResponseDTO;
import com.FieldService.DTO.LoginRequestDTO;
import com.FieldService.DTO.RegisterRequestDTO;
import com.FieldService.DTO.StaffUserRequestDTO;

import com.FieldService.Entity.Customer;
import com.FieldService.Entity.CustomerAuth;
import com.FieldService.Entity.DispatcherAuth;
import com.FieldService.Entity.ManagerAuth;
import com.FieldService.Entity.TechnicianAuth;
import com.FieldService.Entity.UserAuth;

import com.FieldService.ENUM.Role;

import com.FieldService.Repository.CustomerRepository;
import com.FieldService.Repository.UserRepository;

import com.FieldService.Security.EmailLogService;
import com.FieldService.Security.JWTUtil;
import com.FieldService.Security.TokenBlockService;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.UUID;

@Service
public class UserAuthService {

	private final UserRepository userRepository;

	private final CustomerRepository customerRepository;

	private final PasswordEncoder passwordEncoder;

	private final JWTUtil jwtUtil;

	private final EmailLogService emailLogService;

	private final TokenBlockService tokenBlockService;


	public UserAuthService(
			UserRepository userRepository,
			CustomerRepository customerRepository,
			PasswordEncoder passwordEncoder,
			JWTUtil jwtUtil,
			EmailLogService emailLogService,
			TokenBlockService tokenBlockService) {

		this.userRepository =
				userRepository;

		this.customerRepository =
				customerRepository;

		this.passwordEncoder =
				passwordEncoder;

		this.jwtUtil =
				jwtUtil;

		this.emailLogService =
				emailLogService;

		this.tokenBlockService =
				tokenBlockService;
	}


	// =========================================
	// PUBLIC CUSTOMER REGISTRATION
	// =========================================

	@Transactional
	public String register(
			RegisterRequestDTO request) {

		if (userRepository
				.existsByUserEmail(
						request.getUserEmail()
				)) {

			throw new RuntimeException(
					"Email already registered"
			);
		}


		if (customerRepository
				.existsByEmail(
						request.getUserEmail()
				)) {

			throw new RuntimeException(
					"Customer with this email already exists"
			);
		}


		/*
		 * First create the Customer.
		 */
		Customer customer =
				Customer.builder()
						.companyName(
								request.getCompanyName()
						)
						.contactPerson(
								request.getUserName()
						)
						.email(
								request.getUserEmail()
						)
						.phone(
								request.getPhone()
						)
						.active(true)
						.build();


		Customer savedCustomer =
				customerRepository.save(
						customer
				);


		/*
		 * Then create the login account and
		 * link it to that customer.
		 *
		 * Public registration ALWAYS creates
		 * CUSTOMER role.
		 */
		UserAuth user =
				createUserForRole(
						Role.CUSTOMER
				);

		user.setUserName(
				request.getUserName()
		);
		user.setUserEmail(
				request.getUserEmail()
		);
		user.setPhone(
				request.getPhone()
		);
		user.setPassword(
				passwordEncoder.encode(
						request.getPassword()
				)
		);
		user.setCustomerId(
				savedCustomer.getId()
		);


		userRepository.save(user);


		return "Customer registered successfully";
	}


	// =========================================
	// LOGIN
	// =========================================

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
				jwtUtil.generateToken(
						user
				);


		return new AuthResponseDTO(
				token,
				"Login successful"
		);
	}


	// =========================================
	// CURRENT LOGGED-IN USER
	// =========================================

	public CurrentUserResponseDTO getCurrentUser(
			String userEmail) {

		UserAuth user =
				userRepository
						.findByUserEmail(
								userEmail
						)
						.orElseThrow(() ->
								new RuntimeException(
										"Logged-in user not found"
								)
						);


		return CurrentUserResponseDTO
				.builder()
				.id(
						user.getId()
				)
				.userName(
						user.getUserName()
				)
				.userEmail(
						user.getUserEmail()
				)
				.phone(
						user.getPhone()
				)
				.role(
						user.getRole()
				)
				.customerId(
						user.getCustomerId()
				)
				.build();
	}


	public List<CurrentUserResponseDTO> getUsersByRole(
			Role role) {

		return userRepository
				.findByRole(role)
				.stream()
				.map(this::toCurrentUserResponse)
				.toList();
	}


	public List<CurrentUserResponseDTO> getStaffUsers() {

		return userRepository
				.findByRoleIn(
						Arrays.asList(
								Role.MANAGER,
								Role.DISPATCHER,
								Role.TECHNICIAN
						)
				)
				.stream()
				.map(this::toCurrentUserResponse)
				.toList();
	}


	public CurrentUserResponseDTO createStaffUser(
			StaffUserRequestDTO request) {

		if (request.getRole() == null) {
			throw new RuntimeException(
					"Role is required"
			);
		}

		if (request.getRole() == Role.CUSTOMER) {
			throw new RuntimeException(
					"Customer accounts must be created through customer registration"
			);
		}

		if (userRepository
				.existsByUserEmail(
						request.getUserEmail()
				)) {

			throw new RuntimeException(
					"Email already registered"
			);
		}

		UserAuth user =
				createUserForRole(
						request.getRole()
				);

		user.setUserName(request.getUserName());
		user.setUserEmail(request.getUserEmail());
		user.setPhone(request.getPhone());
		user.setPassword(
				passwordEncoder.encode(
						request.getPassword()
				)
		);

		return toCurrentUserResponse(
				userRepository.save(user)
		);
	}


	public CurrentUserResponseDTO createInitialManager(
			StaffUserRequestDTO request) {

		if (userRepository.existsByRole(Role.MANAGER)) {
			throw new RuntimeException(
					"A manager already exists. Login as manager and use /api/user_auth/staff"
			);
		}

		request.setRole(Role.MANAGER);

		return createStaffUser(request);
	}


	private CurrentUserResponseDTO toCurrentUserResponse(
			UserAuth user) {

		return CurrentUserResponseDTO
				.builder()
				.id(user.getId())
				.userName(user.getUserName())
				.userEmail(user.getUserEmail())
				.phone(user.getPhone())
				.role(user.getRole())
				.customerId(user.getCustomerId())
				.build();
	}


	private UserAuth createUserForRole(
			Role role) {

		UserAuth user =
				switch (role) {
					case MANAGER -> new ManagerAuth();
					case DISPATCHER -> new DispatcherAuth();
					case TECHNICIAN -> new TechnicianAuth();
					case CUSTOMER -> new CustomerAuth();
				};

		user.setRole(role);

		return user;
	}


	// =========================================
	// LOGOUT
	// =========================================

	public void logout(
			String token) {

		if (token == null
				|| token.isBlank()) {

			throw new RuntimeException(
					"Token is required"
			);
		}


		tokenBlockService.blockToken(
				token
		);
	}


	// =========================================
	// FORGOT PASSWORD
	// =========================================

	public void forgetPassword(
			String userEmail) {

		UserAuth user =
				userRepository
						.findByUserEmail(
								userEmail
						)
						.orElseThrow(() ->
								new RuntimeException(
										"User not found"
								)
						);


		String token =
				UUID.randomUUID()
						.toString();


		user.setResetToken(
				token
		);


		user.setResetTokenExpiry(
				new Date(
						System.currentTimeMillis()
								+ 15 * 60 * 1000
				)
		);


		userRepository.save(
				user
		);


		emailLogService
				.sendResetPasswordEmail(
						user.getUserEmail(),
						token
				);
	}


	// =========================================
	// RESET PASSWORD
	// =========================================

	public void resetPassword(
			String token,
			String newPassword) {

		UserAuth user =
				userRepository
						.findByResetToken(
								token
						)
						.orElseThrow(() ->
								new RuntimeException(
										"Invalid reset token"
								)
						);


		if (user.getResetTokenExpiry()
				== null
				|| user
				.getResetTokenExpiry()
				.before(
						new Date()
				)) {

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


		user.setResetToken(
				null
		);

		user.setResetTokenExpiry(
				null
		);


		userRepository.save(
				user
		);
	}
}
