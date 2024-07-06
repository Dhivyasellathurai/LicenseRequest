package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.EmailDetails;
import com.example.demo.service.EmailService;

@RestController
@RequestMapping("/smtp")
public class EmailController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/sendMail")
	public String sendMail(@RequestBody Map<String, Object> payload) {
		String[] to = ((String) payload.get("to")).split(",");
		String subject = (String) payload.get("subject");
		String text = (String) payload.get("text");
		String status = emailService.sendSimpleMail(to, subject, text);
		return status;
	}

	@PostMapping("/send/email")
	public String sendMail(@RequestParam("companyName") String companyName, @RequestBody EmailDetails details) {
		return emailService.sendSimpleMail(companyName, details);
	}

	@PostMapping("/sendMailWithAttachment")
	public String sendMailWithAttachment(@RequestBody EmailDetails details) {
		String status = emailService.sendMailWithAttachment(details);

		return status;
	}
}
