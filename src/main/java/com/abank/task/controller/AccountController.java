package com.abank.task.controller;

import com.abank.task.entity.Account;
import com.abank.task.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public List<Account> getAccountsByClientId(@PathVariable Long id) {
        return service.getAccountsByClientId(id);
    }
}
