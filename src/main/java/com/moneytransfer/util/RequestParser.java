package com.moneytransfer.util;

import spark.Request;

import static java.lang.Integer.parseInt;

public class RequestParser {

    private RequestParser() {
    }

    public static int parseCustomerId(final Request request) {
        return parseInt(request.params(":customerId"));
    }

    public static int parseAccountId(final Request request) {
        return parseInt(request.params(":accountId"));
    }
}
