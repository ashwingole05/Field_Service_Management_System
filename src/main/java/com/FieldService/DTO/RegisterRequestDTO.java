package com.FieldService.DTO;

import com.FieldService.ENUM.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

	@NotBlank(message = "User name is required")
	private String userName;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String userEmail;

	@NotBlank(message = "Phone number is required")
	private String phone;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must contain at least 6 characters")
	private String password;

	private Role role;
}