package com.moneytransfer.service;

import com.moneytransfer.model.TransactionRequest;
import com.moneytransfer.model.dto.TransactionDto;

import java.util.List;

public interface TransactionService {

    List<TransactionDto> getTransactions(int accountId);

    TransactionDto transferMoney(TransactionRequest request);
}
