package com.abank.task.repository;

import com.abank.task.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepo extends JpaRepository<Account, Long> {
    List<Account> findAllByClient_Id(Long clientId);
}
