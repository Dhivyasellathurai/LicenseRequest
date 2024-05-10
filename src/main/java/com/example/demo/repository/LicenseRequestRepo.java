package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.LicenseRequest;

public interface LicenseRequestRepo extends JpaRepository<LicenseRequest, UUID> {

	@Query(value = " from LicenseRequest lr where lr.companyName=:companyName ")
	Optional<LicenseRequest> findByCompanyName(@Param("companyName") String companyName);

	@Query(value = "from LicenseRequest lr where lr.licenseKey=:licenseKey")
	Optional<LicenseRequest> findByLicenseKey(@Param("licenseKey") String licenseKey);

	@Query(value = "select * from license_request lr where lr.license_key=:licenseKey", nativeQuery = true)
	Optional<LicenseRequest> findByLicense(String licenseKey);

}
