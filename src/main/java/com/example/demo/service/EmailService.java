package com.example.demo.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DecryptDataDto;
import com.example.demo.entity.EmailDetails;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private LicenseRequestService requestService;

	@Value("$(spring.mail.username)")
	private String sender;

	public String sendSimpleMail(String[] recipient, String Body, String Subject) {

		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(sender);
			mailMessage.setTo(recipient);
			mailMessage.setText(Body);
			mailMessage.setSubject(Subject);

			javaMailSender.send(mailMessage);

			return "Mail Sent Successfully...";
		}

		catch (Exception e) {
			return "Error while Sending Mail";
		}
	}

	public String sendSimpleMail(String CompanyName, EmailDetails sendDetails) {

		try {
			DecryptDataDto dataDto = requestService.getEncryptData(CompanyName);
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(sender);
			mailMessage.setTo(sendDetails.getRecipient());
			mailMessage.setText(
					"EncryptedData :" + dataDto.getEncryptedData() + ", secret Key :" + dataDto.getSecretKey());
			mailMessage.setSubject(sendDetails.getSubject());
			mailMessage.setCc(sendDetails.getCc());
			mailMessage.setBcc(sendDetails.getBcc());

			javaMailSender.send(mailMessage);

			return "Mail Sent Successfully...";
		}

		catch (Exception e) {
			return "Error while Sending Mail";
		}
	}

	public String sendMailWithAttachment(EmailDetails details) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;

		try {

			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(sender);
			mimeMessageHelper.setTo(details.getRecipient());
			mimeMessageHelper.setText(details.getMsgBody());
			mimeMessageHelper.setSubject(details.getSubject());

			FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

			mimeMessageHelper.addAttachment(file.getFilename(), file);

			javaMailSender.send(mimeMessage);
			return "Mail sent Successfully";
		}

		catch (MessagingException e) {

			return "Error while sending mail!!!";
		}
	}
}
