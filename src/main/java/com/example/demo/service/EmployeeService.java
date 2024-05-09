package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.security.ValidationUtil;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public Object addEmployee(Employee employee) {

		if (ValidationUtil.isNullOrEmpty(employee.getName()) == true) {
			return " Name cannot be null";
		}
		if (ValidationUtil.isStartWithUpperCase(employee.getName()) == false) {
			return "Invalid Name, Name should start with Uppercase letter";
		}
		if (ValidationUtil.isNull(employee.getSalary()) == true) {
			return "Invalid Salary";
		}
		if (ValidationUtil.isNullOrEmpty(employee.getPhoneNo()) == true) {
			return "Invalid PhoneNo";
		}
		if (ValidationUtil.isValidEmail(employee.getEmail()) == true) {
			return "Invalid Email";
		}
		if ((employee.getPhoneNo().length()) != 10) {
			return "Invalid phoneNo";
		}
		Optional<Employee> optional = employeeRepository.findByEmail(employee.getEmail());
		if (optional.isPresent()) {
			return "Email Already exist";
		}
		return employeeRepository.save(employee);

	}

	public Object getEmployeeById(int id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		if (employee.isEmpty()) {
			throw new NullPointerException("Employee Id is not found");
		}
		return employee;
	}

	public Object updateEmployee(Employee employee) {
		Optional<Employee> employee1 = employeeRepository.findById(employee.getId());
		if (employee1.isPresent()) {
			employeeRepository.saveAndFlush(employee);
			return "update success";
		} else {
			return "Employee cannot found.";
		}

	}

	public void deleteById(int id) {
		employeeRepository.deleteById(id);
	}
}
