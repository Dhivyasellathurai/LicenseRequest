package com.example.demo.repository;

import com.example.demo.entity.EmailDetails;

public interface EmailService {

	String sendSimpleMail(EmailDetails details);

	String sendMailWithAttachment(EmailDetails details);

}
