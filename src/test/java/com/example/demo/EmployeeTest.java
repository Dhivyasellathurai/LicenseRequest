//package com.example.demo;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.lenient;
//import static org.mockito.Mockito.when;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.example.demo.entity.Employee;
//import com.example.demo.repository.EmployeeRepository;
//import com.example.demo.security.ValidationUtil;
//import com.example.demo.service.EmployeeService;
//
//@ExtendWith(MockitoExtension.class)
//public class EmployeeTest {
//
//	@Mock
//	EmployeeRepository employeeRepository;
//
//	@InjectMocks
//	EmployeeService employeeService;
//
//	@InjectMocks
//	ValidationUtil validationUtil;
//
//	@Test
//	void createEmployeeTest() {
//
//		Employee employee = new Employee(1, "Dhivya", 80000.00, "6789067890", "dhivya123@gmail.com");
//
//		when(employeeRepository.findByEmail("dhivya123@gmail.com")).thenReturn(Optional.empty());
//		when(employeeRepository.save(employee)).thenReturn(employee);
//
//		Object object = employeeService.addEmployee(employee);
//		assertEquals(object, employee);
//
//	}
//
//	@Test
//	void createEmployeeTest1() {
//
//		Employee employee = new Employee(1, "Dhivya", 80000.00, "6789067890", "dhivya123@gmail.com");
//
//		when(employeeRepository.findByEmail("dhivya123@gmail.com")).thenReturn(Optional.of(employee));
//
//		Object object = employeeService.addEmployee(employee);
//		assertEquals(object, "Email Already exist");
//
//	}
//
//	@Test
//	void createEmployeeTest2() {
//
//		Employee employee = new Employee(1, "Dhivya", 80000.00, "", "dhivya123@gmail.com");
//
//		Object object = employeeService.addEmployee(employee);
//		assertEquals(object, "Invalid PhoneNo");
//
//	}
//
//	@Test
//	void getByIdTest() {
//
//		Employee employee = new Employee(1, "Dhivya", 80000.00, "6789067890", "dhivya123@gmail.com");
//		when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
//		Object result = employeeService.getEmployeeById(1);
//		assertEquals(result, Optional.of(employee));
//
//	}
//
//	@Test
//	void getByIdTest2() {
//		Employee employee = new Employee(1, "Dhivya", 80000.00, "6789067890", "dhivya123@gmail.com");
//		lenient().when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
//		when(employeeRepository.findById(2)).thenReturn(Optional.empty());
//		Assertions.assertThrows(NullPointerException.class, () -> employeeService.getEmployeeById(2));
//
//	}
//
//	@Test
//	void updateTest() {
//		Employee employee = new Employee(1, "Dhivya", 80000.00, "6789067890", "dhivya123@gmail.com");
//		Employee employee1 = new Employee(1, "Dhivya", 100000.00, "6789067890", "dhivya123@gmail.com");
//		when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
//		Object result = employeeService.updateEmployee(employee1);
//		assertEquals("update success", result);
//	}
//
//	@Test
//	void updateTest2() {
//		Employee employee = new Employee(1, "Dhivya", 80000.00, "6789067890", "dhivya123@gmail.com");
//		when(employeeRepository.findById(1)).thenReturn(Optional.empty());
//		Object result = employeeService.updateEmployee(employee);
//		assertEquals("Employee cannot found.", result);
//
//	}
//}
