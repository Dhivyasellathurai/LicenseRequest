package com.example.demo.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DecryptDataDto;
import com.example.demo.dto.EncryptDataDto;
import com.example.demo.entity.LicenseRequest;
import com.example.demo.entity.Status;
import com.example.demo.repository.LicenseRequestRepo;
import com.example.demo.security.EncryptionUtill;

@Service
public class LicenseRequestService {

	@Autowired
	private LicenseRequestRepo licenseRequestRepo;

	public String createRequest(LicenseRequest licenseRequest) {
		licenseRequestRepo.save(licenseRequest);
		return generateLicenseKey(licenseRequest.getCompanyName());
	}

	public String generateLicenseKey(String companyName) {
		Optional<LicenseRequest> optional = licenseRequestRepo.findByCompanyName(companyName);
		LicenseRequest licenseReq = optional.get();
		String licenseKey1 = licenseReq.getCompanyName() + "-" + licenseReq.getEmail() + "-"
				+ licenseReq.getRequestId();
		String licenseKey = EncryptionUtill.hashString(licenseKey1);
		licenseReq.setLicenseKey(licenseKey);
		licenseRequestRepo.save(licenseReq);
		return licenseKey;

	}

	public DecryptDataDto getEncryptData(String companyName) throws Exception {
		Optional<LicenseRequest> optional = licenseRequestRepo.findByCompanyName(companyName);
		if (optional.isPresent()) {
			LicenseRequest request = optional.get();
			EncryptDataDto dataDto = EncryptDataDto.builder().email(request.getEmail())
					.licenseKey(request.getLicenseKey()).build();
			DecryptDataDto encryptedData = EncryptionUtill.encrypt(dataDto);
			return encryptedData;
		} else {
			return null;
		}
	}

	public EncryptDataDto getDecryptData(DecryptDataDto dataDto) throws Exception {
		try {
			EncryptDataDto decryptData = EncryptionUtill.decrypt(dataDto);
			return decryptData;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String validateLicense(String licenseKey) {
		Optional<LicenseRequest> optional = licenseRequestRepo.findByLicenseKey(licenseKey);
		if (optional.isPresent()) {
			LicenseRequest licenseRequest = optional.get();
			licenseRequest.setStatus(Status.APPROVED);
			licenseRequest.setActivationDate(new Date());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(licenseRequest.getActivationDate());
			calendar.add(Calendar.YEAR, 1);
			Date expiryDate = calendar.getTime();
			licenseRequest.setExpiryDate(expiryDate);
			licenseRequestRepo.save(licenseRequest);
			return "license validate successfully and approved";
		} else {
			return "License Rejected";
		}
	}
}
