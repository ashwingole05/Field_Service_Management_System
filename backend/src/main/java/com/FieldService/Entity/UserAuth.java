package com.FieldService.Entity;

import com.FieldService.ENUM.Role;

import jakarta.persistence.*;

import lombok.*;

import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserAuth {

	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "user_auth_seq"
	)
	@SequenceGenerator(
			name = "user_auth_seq",
			sequenceName = "user_auth_seq",
			allocationSize = 1
	)
	private Long id;

	@Column(nullable = false)
	private String userName;

	@Column(
			unique = true,
			nullable = false
	)
	private String userEmail;

	private String phone;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	/*
	 * Only CUSTOMER accounts use this field.
	 *
	 * Multiple UserAuth records are allowed to point
	 * to the same Customer in the future if one company
	 * has multiple users.
	 */
	private Long customerId;

	private String resetToken;

	private Date resetTokenExpiry;
}
