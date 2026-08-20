package com.FieldService.Security;

import com.FieldService.DTO.EmailLogDTO;
import com.FieldService.Entity.EmailLog;
import com.FieldService.Repository.EmailLogRepository;

import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;

@Service
public class EmailLogService {

	private final JavaMailSender javaMailSender;
	private final EmailLogRepository emailLogRepository;

	public EmailLogService(
			JavaMailSender javaMailSender,
			EmailLogRepository emailLogRepository
	) {
		this.javaMailSender = javaMailSender;
		this.emailLogRepository = emailLogRepository;
	}

	public String sendResetPasswordEmail(
			String to,
			String token
	) {

		String resetPasswordLink =
				"http://localhost:3000/reset-password?token="
						+ token;

		SimpleMailMessage message =
				new SimpleMailMessage();

		message.setTo(to);
		message.setSubject("KEYSTONE - Reset Your Password");

		message.setText(
				"Click the following link to reset your password:\n\n"
						+ resetPasswordLink
		);

		javaMailSender.send(message);

		EmailLog log = new EmailLog(
				to,
				"KEYSTONE - Reset Your Password",
				resetPasswordLink
		);

		log.setSentStatus(true);

		emailLogRepository.save(log);

		return "Reset password link sent successfully";
	}

	public String notify(EmailLogDTO emailLog) {

		boolean sentStatus = false;

		try {

			MimeMessage message =
					javaMailSender.createMimeMessage();

			MimeMessageHelper helper =
					new MimeMessageHelper(
							message,
							true
					);

			helper.setTo(
					emailLog.getRecepientEmail()
			);

			helper.setSubject(
					emailLog.getSubject()
			);

			helper.setText(
					emailLog.getBody(),
					true
			);

			javaMailSender.send(message);

			sentStatus = true;

		} catch (Exception e) {

			sentStatus = false;
		}

		EmailLog log = new EmailLog(
				emailLog.getRecepientEmail(),
				emailLog.getSubject(),
				emailLog.getBody()
		);

		log.setSentStatus(sentStatus);

		emailLogRepository.save(log);

		return sentStatus
				? "Email sent successfully"
				: "Email not sent";
	}
}