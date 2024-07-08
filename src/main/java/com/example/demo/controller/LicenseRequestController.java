package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DecryptDataDto;
import com.example.demo.dto.EncryptDataDto;
import com.example.demo.entity.ExpiredStatus;
import com.example.demo.entity.LicenseRequest;
import com.example.demo.repository.LicenseRequestRepo;
import com.example.demo.service.EmailService;
import com.example.demo.service.LicenseRequestService;

@RestController
@RequestMapping("/api/license")
public class LicenseRequestController {

	@Autowired
	private LicenseRequestService requestService;

	@Autowired
	private LicenseRequestRepo licenseRequestRepo;

	@Autowired
	private EmailService emailService;

	@PostMapping("/create/request")
	public String sendRequest(@RequestBody LicenseRequest licenseRequest) {
		return requestService.createRequest(licenseRequest);
	}

	@GetMapping("/generate/{companyName}")
	public DecryptDataDto generateEncryptedData(@PathVariable("companyName") String companyName) throws Exception {
		DecryptDataDto dataDto = requestService.getEncryptData(companyName);
		return dataDto;
	}

	@PostMapping("/decrypt")
	public EncryptDataDto decrypt(@RequestBody DecryptDataDto encrypt) throws Exception {
		return requestService.getDecryptData(encrypt);
	}

	@PutMapping("/validate")
	public String validateLicense(@RequestParam String licenseKey) {
		try {
			return requestService.validateLicense(licenseKey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Scheduled(fixedRate = 86400000)
	public void validateExpiryDate() {
		LocalDate date = LocalDate.now();
		List<LicenseRequest> expiredRecords = licenseRequestRepo.findAll();
		List<String> results = new ArrayList<>();

		for (LicenseRequest request : expiredRecords) {
			String[] mail = { request.getCommonEmail(), request.getCompanyEmail() };
			LocalDate expiryDate = request.getExpiryDate();
			LocalDate gracePeriod = expiryDate.plusDays(request.getGracePeriod());
			LocalDate alertDate = expiryDate.minusDays(15);
			if (expiryDate.isAfter(date) && date.equals(alertDate)) {
				results.add(request.getCompanyName() + "License is not expired");
				request.setExpiredStatus(ExpiredStatus.NOT_EXPIRED);
				emailService.sendSimpleMail(mail,
						"Hi, Hari , your company license was expired in 15 days , please renew asap",
						"License expired soon");
			} else if (expiryDate.isBefore(date) && gracePeriod.isAfter(date)) {
				results.add(request.getCompanyName() + "License is expired, It is on the Grace period");
				request.setExpiredStatus(ExpiredStatus.IN_GRASSPERIOD);
				emailService.sendSimpleMail(mail,
						"Hi, Hari , your company license was expired, and your company is on the notice period, please renew asap",
						"License expired soon");
			} else {
				results.add(request.getCompanyName() + " License Expired and Grace period is also finished");
				request.setExpiredStatus(ExpiredStatus.EXPIRED);
				emailService.sendSimpleMail(mail, "Hi, Hari , your company license was expired ", "License expired ");
			}
			licenseRequestRepo.save(request);
		}
		System.out.print(results);
	}

}
