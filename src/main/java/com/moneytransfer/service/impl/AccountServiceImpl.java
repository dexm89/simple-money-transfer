package com.moneytransfer.service.impl;

import com.moneytransfer.exception.DataNotFoundException;
import com.moneytransfer.exception.DataSavingErrorException;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.dto.AccountDto;
import com.moneytransfer.repository.AccountRepository;
import com.moneytransfer.service.AccountService;

import java.util.List;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    public AccountServiceImpl(final AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AccountDto> getAccounts(final int customerId) {
        List<AccountDto> customers = repository.findAllByCustomerId(customerId);
        if (customers.isEmpty()) {
            throw new DataNotFoundException("Accounts not found");
        }
        return customers;
    }

    @Override
    public AccountDto getAccount(final int accountId) {
        return repository.find(accountId)
                .orElseThrow(() -> new DataNotFoundException("Account not found by id " + accountId));
    }

    @Override
    public AccountDto addAccount(final int customerId, final Account account) {
        return repository.save(customerId, account)
                .orElseThrow(() -> new DataSavingErrorException("Account hasn't been saved"));
    }
}
