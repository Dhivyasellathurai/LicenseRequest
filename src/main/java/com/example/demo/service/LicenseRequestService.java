package com.example.demo.service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DecryptDataDto;
import com.example.demo.dto.EncryptDataDto;
import com.example.demo.entity.LicenseRequest;
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

	public Object getEncryptData(String companyName) throws NoSuchAlgorithmException {
		SecretKey key = EncryptionUtill.generateSecretKey();
		Optional<LicenseRequest> optional = licenseRequestRepo.findByCompanyName(companyName);
		if (optional.isPresent()) {
			LicenseRequest request = optional.get();
			EncryptDataDto dataDto = EncryptDataDto.builder().email(request.getEmail())
					.licenseKey(request.getLicenseKey()).build();
			String encryptedData = EncryptionUtill.encrypt(dataDto);
			DecryptDataDto dataDto2 = new DecryptDataDto();
			dataDto2.setEncryptedData(encryptedData);
			dataDto2.setSecretKey(EncryptionUtill.secretKeyToString(key));
			return dataDto2;

		} else {
			return "Company Not found";
		}
	}

	public EncryptDataDto getDecryptData(DecryptDataDto dataDto) throws NoSuchAlgorithmException {
		EncryptDataDto decryptData = EncryptionUtill.decrypt(dataDto);
		return decryptData;
	}
}
