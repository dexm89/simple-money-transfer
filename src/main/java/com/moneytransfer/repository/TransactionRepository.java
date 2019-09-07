package com.moneytransfer.repository;

import com.moneytransfer.model.dto.TransactionDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    List<TransactionDto> findAllByCustomerId(int accountId);

    Optional<TransactionDto> transferMoney(int accIdToDebit, int accIdToCredit, BigDecimal amount);
}
