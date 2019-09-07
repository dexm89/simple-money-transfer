package com.moneytransfer.service;

import com.moneytransfer.model.Account;
import com.moneytransfer.model.dto.AccountDto;

import java.util.List;

public interface AccountService {

    List<AccountDto> getAccounts(int customerId);

    AccountDto getAccount(int accountId);

    AccountDto addAccount(int customerId, Account account);
}
