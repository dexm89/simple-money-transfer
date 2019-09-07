package com.moneytransfer.repository;

import com.moneytransfer.model.Account;
import com.moneytransfer.model.dto.AccountDto;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    List<AccountDto> findAllByCustomerId(int customerId);

    Optional<AccountDto> find(int accountId);

    Optional<AccountDto> save(int customerId, Account account);
}
