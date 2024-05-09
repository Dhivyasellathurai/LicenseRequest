package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.entity.BankAccount;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.AccountService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = BankAccount.class)
public class BankAccountTest {

	@Mock
	AccountRepository accountRepository;

	@InjectMocks
	AccountService accountService;

	@Test
	void createAccountTest() {

		BankAccount account = new BankAccount(1, "8945R", "Dhivya", 500.00);
		BankAccount account1 = new BankAccount(2, "DF$%", "Divya", 500.00);
		BankAccount account2 = new BankAccount(2, "DF$%2", "", 500.00);

		when(accountRepository.save(any(BankAccount.class))).thenReturn(account);

		String message = accountService.createAccount(account);
		String message1 = accountService.createAccount(account1);
		String message2 = accountService.createAccount(account2);

		verify(accountRepository, times(1)).save(account);

		assertNotNull(account);
		assertNotNull(account1);

		assertEquals(message, "Account created successfully" + "\n" + account);
		assertEquals(message1, "Invalid data for create new account");
		assertEquals(message2, "Account number , Holder name  and Balance amount are cannot be null.");
	}

	@Test
	void depositTest() {

		BankAccount account = new BankAccount(1, "89", "dhivya", 500.00);
		BankAccount account1 = new BankAccount(1, "89", "dhivya", 1000.00);
		BankAccount account2 = new BankAccount(2, "90", "babu", 1000.00);

		when(accountRepository.getByAccountNo("89")).thenReturn(Optional.of(account));
		when(accountRepository.getByAccountNo("90")).thenReturn(Optional.empty());

		double account3 = accountService.deposit(account1);
		double account4 = accountService.deposit(account2);

		assertEquals(account3, 1500);
		assertEquals(account4, 1000);

	}

	@Test
	void checkBalanceTest() {

		BankAccount account = new BankAccount(1, "89", "dhivya", 500.00);

		when(accountRepository.getByAccountNo("89")).thenReturn(Optional.of(account));

		Object account2 = accountService.checkBalance("89");
		Object account3 = accountService.checkBalance("90");

		assertEquals(account2, 500.00);
		assertEquals(account3, "No account found");

	}

	@Test
	void withDrawTest() {

		BankAccount account = new BankAccount(1, "89", "dhivya", 5000.00);

		when(accountRepository.getByAccountNo("89")).thenReturn(Optional.of(account));

		Object account1 = accountService.withDraw("89", 2000.00);
		assertEquals(account1, 3000.00);

		Object account2 = accountService.withDraw("89", 6000.00);
		assertEquals(account2, "Insufficient Balance");

		Object account3 = accountService.withDraw("90", 800.00);
		assertEquals(account3, "Could not find the account");

	}

	@Test
	void deleteAccount() {
		BankAccount account = new BankAccount(1, "89", "dhivya", 00.00);
		BankAccount account1 = new BankAccount(2, "90", "dhivya", 1000.00);

		when(accountRepository.getByAccountNo("89")).thenReturn(Optional.of(account));
		when(accountRepository.getByAccountNo("90")).thenReturn(Optional.of(account1));

		Object account2 = accountService.deleteAccount("89");
		assertEquals(account2, "account deleted successfully");

		Object account3 = accountService.deleteAccount("90");
		assertEquals(account3, "Could not delete the account");

		Object account4 = accountService.deleteAccount("99");
		assertEquals(account4, "Could not find the account");

	}

}
