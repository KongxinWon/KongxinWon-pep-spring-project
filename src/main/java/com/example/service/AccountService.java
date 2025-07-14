package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account register(Account acc) {
        return accountRepository.save(acc);
    }

    public Optional<Account> login(String username, String password) {
        return accountRepository.findByUsernameAndPassword(username, password);
    }

    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }
}
