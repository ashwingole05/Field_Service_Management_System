package com.FieldService.Controller;

import com.FieldService.DTO.EmailLogDTO;
import com.FieldService.Security.EmailLogService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email_log")
public class EmailLogController {

	private final EmailLogService emailLogService;

	public EmailLogController(
			EmailLogService emailLogService
	) {
		this.emailLogService = emailLogService;
	}

	@PostMapping("/resetPasswordEmail")
	public ResponseEntity<String> sendResetPasswordEmail(
			@RequestParam String to,
			@RequestParam String token
	) {

		return ResponseEntity.ok(
				emailLogService.sendResetPasswordEmail(
						to,
						token
				)
		);
	}

	@PostMapping("/notify")
	public ResponseEntity<String> notification(
			@RequestBody EmailLogDTO emailLog
	) {

		return ResponseEntity.ok(
				emailLogService.notify(emailLog)
		);
	}
}