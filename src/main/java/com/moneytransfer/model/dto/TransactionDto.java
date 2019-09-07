package com.moneytransfer.model.dto;

import java.math.BigDecimal;

public class TransactionDto {

    private final Integer transactionId;
    private final Integer fromAccount;
    private final Integer toAccount;
    private final BigDecimal amount;

    public TransactionDto(final Integer transactionId,
                          final Integer fromAccount,
                          final Integer toAccount,
                          final BigDecimal amount) {
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public Integer getFromAccount() {
        return fromAccount;
    }

    public Integer getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "{"
                + "\"transactionId\":\"" + transactionId + "\""
                + ",\"fromAccount\":\"" + fromAccount + "\""
                + ",\"toAccount\":\"" + toAccount + "\""
                + ",\"amount\":\"" + amount + "\""
                + "}";
    }
}
