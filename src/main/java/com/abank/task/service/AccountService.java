package com.abank.task.service;

import com.abank.task.entity.Account;
import com.abank.task.repository.AccountRepo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AccountService implements IAccountService {

    private final AccountRepo accountRepo;

    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public List<Account> getAccountsByClientId(Long clientId) {
        return accountRepo.findAllByClient_Id(clientId);
    }
}
