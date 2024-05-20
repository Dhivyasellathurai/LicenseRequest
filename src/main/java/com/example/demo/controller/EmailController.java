package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.EmailDetails;
import com.example.demo.service.EmailService;

@RestController
@RequestMapping("/smtp")
public class EmailController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/sendMail")
	public String sendMail() {
		String status = emailService.sendSimpleMail("adhanalakshmi230@gmail.com", "Hi dhanam", "This is dhivya");
		return status;
	}

	@PostMapping("/send/email")
	public String sendMail(@RequestBody EmailDetails details) {
		return emailService.sendSimpleMail(details);
	}

	@PostMapping("/sendMailWithAttachment")
	public String sendMailWithAttachment(@RequestBody EmailDetails details) {
		String status = emailService.sendMailWithAttachment(details);

		return status;
	}
}
