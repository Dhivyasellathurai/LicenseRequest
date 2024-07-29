package com.example.demo.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepo;
import com.example.demo.security.PasswordEncrypt;

import jakarta.servlet.http.HttpSession;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepo repo;

	public void createCustomer(Customer request) throws NoSuchAlgorithmException {
		request.setPassword(PasswordEncrypt.getEncryptedPassword(request.getPassword()));
		repo.saveAndFlush(request);

	}

	public Optional<Customer> getById(UUID id) {
		return repo.findById(id);
	}

	public List<Customer> getAll() {
		return repo.findAll();
	}

	public void deleteById(UUID id) {
		repo.deleteById(id);
	}

}
