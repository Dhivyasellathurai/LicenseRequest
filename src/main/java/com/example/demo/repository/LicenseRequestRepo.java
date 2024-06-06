package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.LicenseRequest;

@Repository
public interface LicenseRequestRepo extends JpaRepository<LicenseRequest, UUID> {

	@Query(value = " from LicenseRequest lr where lr.companyName=:companyName ")
	Optional<LicenseRequest> findByCompanyName(@Param("companyName") String companyName);

	@Query(value = "select * FROM demo.license_request WHERE license_key=:licenseKey", nativeQuery = true)
	Optional<LicenseRequest> findByLicenseKey(String licenseKey);

}
