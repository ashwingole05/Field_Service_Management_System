package com.FieldService.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

	@NotBlank(message = "Name is required")
	private String userName;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email address")
	private String userEmail;

	@NotBlank(message = "Phone number is required")
	@Pattern(
			regexp = "^[0-9]{10}$",
			message = "Phone number must contain 10 digits"
	)
	private String phone;

	/*
	 * Required because Customer currently requires
	 * companyName.
	 */
	@NotBlank(message = "Company name is required")
	private String companyName;

	@NotBlank(message = "Password is required")
	@Size(
			min = 6,
			message = "Password must contain at least 6 characters"
	)
	private String password;
}