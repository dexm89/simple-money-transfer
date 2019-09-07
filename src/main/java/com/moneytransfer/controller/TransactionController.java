package com.moneytransfer.controller;

import com.moneytransfer.model.TransactionRequest;
import com.moneytransfer.service.TransactionService;
import spark.Request;
import spark.Response;
import spark.Route;

import static com.moneytransfer.util.JsonUtils.fromJson;
import static com.moneytransfer.util.RequestParser.parseAccountId;

public class TransactionController {

    public static final String TRANSACTIONS_URI = "/transactions";

    private final TransactionService transactionService;

    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public Route getTransactions() {
        return (Request request, Response response) -> transactionService.getTransactions(parseAccountId(request));
    }

    public Route transferMoney() {
        return (Request request, Response response) -> transactionService.transferMoney(getTransactionRequest(request));
    }

    private TransactionRequest getTransactionRequest(final Request request) {
        return fromJson(request.body(), TransactionRequest.class);
    }
}
