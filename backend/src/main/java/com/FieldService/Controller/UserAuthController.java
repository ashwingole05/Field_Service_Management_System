package com.FieldService.Controller;

import com.FieldService.DTO.AuthResponseDTO;
import com.FieldService.DTO.CurrentUserResponseDTO;
import com.FieldService.DTO.LoginRequestDTO;
import com.FieldService.DTO.RegisterRequestDTO;
import com.FieldService.DTO.StaffUserRequestDTO;
import com.FieldService.ENUM.Role;

import com.FieldService.Service.UserAuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user_auth")
public class UserAuthController {

	private final UserAuthService userAuthService;


	public UserAuthController(
			UserAuthService userAuthService) {

		this.userAuthService =
				userAuthService;
	}


	// =========================================
	// PUBLIC CUSTOMER REGISTER
	// =========================================

	@PostMapping("/register")
	public ResponseEntity<String> register(
			@Valid
			@RequestBody
			RegisterRequestDTO register) {

		return ResponseEntity.ok(
				userAuthService.register(
						register
				)
		);
	}


	// =========================================
	// LOGIN
	// =========================================

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(
			@Valid
			@RequestBody
			LoginRequestDTO login) {

		return ResponseEntity.ok(
				userAuthService.login(
						login
				)
		);
	}


	// =========================================
	// CURRENT USER
	// =========================================

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/me")
	public ResponseEntity<CurrentUserResponseDTO>
	getCurrentUser(
			Authentication authentication) {

		return ResponseEntity.ok(
				userAuthService
						.getCurrentUser(
								authentication.getName()
						)
		);
	}


	@PreAuthorize("hasAuthority('VIEW_USER') or hasAuthority('ASSIGN_WO')")
	@GetMapping("/technicians")
	public ResponseEntity<java.util.List<CurrentUserResponseDTO>>
	getTechnicians() {

		return ResponseEntity.ok(
				userAuthService
						.getUsersByRole(
								Role.TECHNICIAN
						)
		);
	}


	@PreAuthorize("hasAuthority('VIEW_USER')")
	@GetMapping("/staff")
	public ResponseEntity<java.util.List<CurrentUserResponseDTO>>
	getStaffUsers() {

		return ResponseEntity.ok(
				userAuthService.getStaffUsers()
		);
	}


	@PreAuthorize("hasAuthority('CREATE_USER')")
	@PostMapping("/staff")
	public ResponseEntity<CurrentUserResponseDTO>
	createStaffUser(
			@Valid
			@RequestBody
			StaffUserRequestDTO request) {

		return ResponseEntity.ok(
				userAuthService
						.createStaffUser(request)
		);
	}


	@PostMapping("/setup-manager")
	public ResponseEntity<CurrentUserResponseDTO>
	createInitialManager(
			@Valid
			@RequestBody
			StaffUserRequestDTO request) {

		return ResponseEntity.ok(
				userAuthService
						.createInitialManager(request)
		);
	}


	// =========================================
	// LOGOUT
	// =========================================

	@PostMapping("/logout")
	public ResponseEntity<String> logout(
			HttpServletRequest request) {

		String header =
				request.getHeader(
						"Authorization"
				);


		if (header == null
				|| !header.startsWith(
				"Bearer "
		)) {

			return ResponseEntity
					.badRequest()
					.body(
							"Bearer token is missing"
					);
		}


		String token =
				header.substring(
						7
				);


		userAuthService.logout(
				token
		);


		return ResponseEntity.ok(
				"Logged out successfully"
		);
	}


	// =========================================
	// FORGOT PASSWORD
	// =========================================

	@PostMapping("/forgetPassword")
	public ResponseEntity<String>
	forgetPassword(
			@RequestParam
			String userEmail) {

		userAuthService
				.forgetPassword(
						userEmail
				);


		return ResponseEntity.ok(
				"Reset mail sent on your Email"
		);
	}


	// =========================================
	// RESET PASSWORD
	// =========================================

	@PostMapping("/resetPassword")
	public ResponseEntity<String>
	resetPassword(
			@RequestParam
			String token,

			@RequestParam
			String newPassword) {

		userAuthService
				.resetPassword(
						token,
						newPassword
				);


		return ResponseEntity.ok(
				"Password reset successfully"
		);
	}
}
