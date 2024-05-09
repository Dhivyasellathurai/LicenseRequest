package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.BankAccount;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount, Integer> {

	@Query(value = "from BankAccount as ba where ba.accountNo=:accountNo")
	Optional<BankAccount> getByAccountNo(@Param(value = "accountNo") String accountNo);

}
