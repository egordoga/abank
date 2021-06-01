package com.abank.task.service;

import com.abank.task.entity.Account;

import java.util.List;

public interface IAccountService {
    List<Account> getAccountsByClientId(Long clientId);
}
