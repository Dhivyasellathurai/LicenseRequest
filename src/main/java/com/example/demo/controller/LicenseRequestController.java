package com.example.demo.controller;

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
		Date date = new Date();
		List<LicenseRequest> expiredRecords = licenseRequestRepo.findAll();
		List<String> results = new ArrayList<>();
		String[] mail = { "hariblockdevil2@gmail.com", "harikaranthiyagarajan@gmail.com" };
		for (LicenseRequest request : expiredRecords) {
			Date expiryDate = request.getExpiryDate();
			Date gracePeriod = addDays(expiryDate, request.getGracePeriod());
			if (expiryDate.after(date)) {
				results.add(request.getCompanyName() + "License is not expired");
				request.setExpiredStatus(ExpiredStatus.NOT_EXPIRED);
			} else if (expiryDate.before(date) && gracePeriod.after(date)) {
				results.add(request.getCompanyName() + "License is expired, It is on the Grace period");
				request.setExpiredStatus(ExpiredStatus.IN_GRASSPERIOD);
				emailService.sendSimpleMail(mail,
						"Hi, Hari , your company license was expiring soon , please renew before the grass period",
						"License expired soon");
			} else {
				results.add(request.getCompanyName() + "License Expired and Grace period is also finished");
				request.setExpiredStatus(ExpiredStatus.EXPIRED);
				emailService.sendSimpleMail(mail,
						"Hi, Hari , your company license was expired , please renew before the grass period",
						"License expired ");
			}
			licenseRequestRepo.save(request);
		}
		System.out.print(results);
	}

	private Date addDays(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return calendar.getTime();
	}
}
