package com.example.demo.entity;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "license_request")
public class LicenseRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID requestId;

	private String companyName;

	private String address;

	private String contactNo;

	private String commonEmail;
	
	private String companyEmail;

	private int gracePeriod;

	private Date expiryDate;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Enumerated(EnumType.STRING)
	private ExpiredStatus expiredStatus;

	private Date activationDate;

	private String licenseKey;

}
