package com.moneytransfer.model.dto;

import java.math.BigDecimal;

public class AccountDto {

    private final Integer accountId;
    private final Integer ownerId;
    private final String currency;
    private final BigDecimal balance;

    public AccountDto(final Integer accountId, final Integer ownerId, final String currency, final BigDecimal balance) {
        this.accountId = accountId;
        this.ownerId = ownerId;
        this.currency = currency;
        this.balance = balance;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "{"
                + "\"accountId\":\"" + accountId + "\""
                + ",\"ownerId\":\"" + ownerId + "\""
                + ",\"currency\":\"" + currency + "\""
                + ",\"balance\":\"" + balance + "\""
                + "}";
    }
}
