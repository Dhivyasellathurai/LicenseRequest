package com.example.demo.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;

@RestController
public class CustomerController {
	@Autowired
	private CustomerService customerService;

	@PostMapping("/create")
	public void createProduct(@RequestBody Customer request) throws NoSuchAlgorithmException {
		customerService.createCustomer(request);
	}

	@GetMapping("/getById/{id}")
	public Optional<Customer> getById(@PathVariable("id") UUID id) {
		return customerService.getById(id);
	}

	@GetMapping("/getAll")
	public List<Customer> getAll() {
		return customerService.getAll();
	}

	@PutMapping("/update")
	public void update(@RequestBody Customer request) throws NoSuchAlgorithmException {
		customerService.createCustomer(request);
	}

	@DeleteMapping("/delete/{id}")
	public void deleteById(@PathVariable("id") UUID id) {
		customerService.deleteById(id);
	}
}
