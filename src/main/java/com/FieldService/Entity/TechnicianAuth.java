package com.FieldService.Entity;

import com.FieldService.ENUM.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "technician_users")
@NoArgsConstructor
public class TechnicianAuth extends UserAuth {

	@PrePersist
	@PreUpdate
	private void applyRole() {
		setRole(Role.TECHNICIAN);
	}
}
