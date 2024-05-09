package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.BankAccount;
import com.example.demo.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	public String createAccount(BankAccount account) {

		if (account.getAccountNo() == "" || account.getAccountHolderName() == "" || account.getBalance() <= 0) {
			return "Account number , Holder name  and Balance amount are cannot be null.";
		}

		if (account.getAccountNo().length() == 5 && account.getBalance() >= 500
				&& Character.isUpperCase(account.getAccountHolderName().charAt(0))) {

			accountRepository.save(account);
			return "Account created successfully" + "\n" + account;

		} else {

			return "Invalid data for create new account";
		}
	}

	public double deposit(BankAccount bankAccount) {
		Optional<BankAccount> account = accountRepository.getByAccountNo(bankAccount.getAccountNo());
		if (account.isPresent()) {
			BankAccount account1 = account.get();
			account1.setBalance(account1.getBalance() + bankAccount.getBalance());
			accountRepository.save(account1);
			return account1.getBalance();
		} else {
			accountRepository.save(bankAccount);
			return bankAccount.getBalance();
		}

	}

	public Object checkBalance(String accountNo) {

		Optional<BankAccount> account = accountRepository.getByAccountNo(accountNo);
		if (account.isPresent()) {
			return account.get().getBalance();
		} else {
			return "No account found";
		}
	}

	public Object withDraw(String accountNo, double amount) {
		Optional<BankAccount> account = accountRepository.getByAccountNo(accountNo);
		if (account.isPresent()) {
			BankAccount account1 = account.get();
			if (amount <= account1.getBalance()) {
				account1.setBalance(account1.getBalance() - amount);
				accountRepository.saveAndFlush(account1);
				return account1.getBalance();
			} else {
				return "Insufficient Balance";
			}

		} else {
			return "Could not find the account";
		}
	}

	public Object deleteAccount(String accountNo) {
		Optional<BankAccount> account = accountRepository.getByAccountNo(accountNo);
		if (account.isPresent()) {
			BankAccount account2 = account.get();
			if (account2.getBalance() == 0) {
				accountRepository.deleteById(account2.getAccountId());
				return "account deleted successfully";
			} else {
				return "Could not delete the account";
			}
		}
		return "Could not find the account";
	}
}