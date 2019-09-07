package com.moneytransfer.model;

import java.math.BigDecimal;

public class TransactionRequest {

    private final Integer accIdToDebit;
    private final Integer accIdToCredit;
    private final BigDecimal amount;

    public TransactionRequest(final Integer accIdToDebit, final Integer accIdToCredit, final BigDecimal amount) {
        this.accIdToDebit = accIdToDebit;
        this.accIdToCredit = accIdToCredit;
        this.amount = amount;
    }

    public Integer getAccIdToDebit() {
        return accIdToDebit;
    }

    public Integer getAccIdToCredit() {
        return accIdToCredit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "{"
                + "\"accIdToDebit\":\"" + accIdToDebit + "\""
                + ",\"accIdToCredit\":\"" + accIdToCredit + "\""
                + ",\"amount\":\"" + amount + "\""
                + "}";
    }
}
