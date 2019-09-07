package com.moneytransfer.controller;

import com.moneytransfer.model.Account;
import com.moneytransfer.model.dto.AccountDto;
import com.moneytransfer.service.AccountService;
import spark.Request;
import spark.Response;
import spark.Route;

import static com.moneytransfer.util.JsonUtils.fromJson;
import static com.moneytransfer.util.RequestParser.parseAccountId;
import static com.moneytransfer.util.RequestParser.parseCustomerId;
import static org.eclipse.jetty.http.HttpHeader.LOCATION;
import static org.eclipse.jetty.http.HttpStatus.CREATED_201;

public class AccountController {

    public static final String ACCOUNTS_URI = "/accounts";
    public static final String ACCOUNT_ID_PATH = "/:accountId";

    private final AccountService accountService;

    public AccountController(final AccountService accountService) {
        this.accountService = accountService;
    }

    public Route getAccounts() {
        return (Request request, Response response) -> accountService.getAccounts(parseCustomerId(request));
    }

    public Route getAccount() {
        return (Request request, Response response) ->
                accountService.getAccount(parseAccountId(request));
    }

    public Route addAccount() {
        return (Request request, Response response) -> {
            AccountDto newAccount = accountService.addAccount(parseCustomerId(request), getAccount(request));

            response.status(CREATED_201);
            response.header(LOCATION.asString(), getLocation(request, newAccount.getAccountId()));
            return newAccount;
        };
    }

    private String getLocation(final Request request, final int accountId) {
        return request.url().replace(request.uri(), ACCOUNTS_URI + "/" + accountId);
    }

    private Account getAccount(final Request request) {
        return fromJson(request.body(), Account.class);
    }
}
