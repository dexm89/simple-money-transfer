package com.moneytransfer.model;

import java.math.BigDecimal;

public class Account {

    private final String currency;
    private BigDecimal balance;

    public Account(final String currency) {
        this.currency = currency;
    }

    public Account(final String currency, final BigDecimal balance) {
        this.currency = currency;
        this.balance = balance;
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
                + "\"currency\":\"" + currency + "\""
                + ",\"balance\":\"" + balance + "\""
                + "}";
    }
}
