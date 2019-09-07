package com.moneytransfer.service.impl;

import com.moneytransfer.exception.DataNotFoundException;
import com.moneytransfer.exception.TransferMoneyErrorException;
import com.moneytransfer.model.TransactionRequest;
import com.moneytransfer.model.dto.TransactionDto;
import com.moneytransfer.repository.TransactionRepository;
import com.moneytransfer.service.TransactionService;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    public TransactionServiceImpl(final TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TransactionDto> getTransactions(final int accountId) {
        List<TransactionDto> transactions = repository.findAllByCustomerId(accountId);
        if (transactions.isEmpty()) {
            throw new DataNotFoundException("Transactions not found");
        }
        return transactions;
    }

    @Override
    public TransactionDto transferMoney(final TransactionRequest request) {
        return repository.transferMoney(request.getAccIdToDebit(), request.getAccIdToCredit(), request.getAmount())
                .orElseThrow(() ->
                        new TransferMoneyErrorException(request.getAccIdToDebit(), request.getAccIdToCredit()));
    }
}
