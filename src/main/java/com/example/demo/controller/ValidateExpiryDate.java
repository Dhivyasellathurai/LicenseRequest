package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ValidateExpiryDate implements CommandLineRunner {

	@Autowired
	private LicenseRequestController controller;

	@Override
	public void run(String... args) throws Exception {
		controller.validateExpiryDate();
	}

}
