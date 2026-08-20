package com.FieldService.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String userEmail;

	@NotBlank(message = "Password is required")
	private String password;
}