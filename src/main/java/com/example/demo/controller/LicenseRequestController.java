package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DecryptDataDto;
import com.example.demo.entity.LicenseRequest;
import com.example.demo.service.LicenseRequestService;

@RestController
public class LicenseRequestController {

	@Autowired
	private LicenseRequestService requestService;

	@PostMapping("/create/request")
	public String sendRequest(@RequestBody LicenseRequest licenseRequest) {
		return requestService.createRequest(licenseRequest);
	}

	@GetMapping("/generate/{companyName}")
	public Object generateEncryptedData(@PathVariable("companyName") String companyName) throws Exception {
		return requestService.getEncryptData(companyName);
	}

	@PostMapping("/decrypt")
	public Object decrypt(@RequestBody DecryptDataDto encrypt) throws Exception {
		return requestService.getDecryptData(encrypt);
	}
}
