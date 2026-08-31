package com.FieldService.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String recepientEmail;

	private String subject;

	@Column(length = 5000)
	private String body;

	private LocalDateTime sentAt;

	private boolean sentStatus;

	@PrePersist
	public void prePersist() {

		if (sentAt == null) {
			sentAt = LocalDateTime.now();
		}
	}

	public EmailLog(
			String recepientEmail,
			String subject,
			String body
	) {

		this.recepientEmail = recepientEmail;
		this.subject = subject;
		this.body = body;
		this.sentAt = LocalDateTime.now();
	}
}