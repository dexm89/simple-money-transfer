package com.moneytransfer.error.handler;

import com.moneytransfer.error.ApiError;
import com.moneytransfer.exception.CurrencyMismatchingException;
import com.moneytransfer.exception.DataNotFoundException;
import com.moneytransfer.util.JsonUtils;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.jetty.http.HttpStatus.*;
import static spark.Spark.exception;
import static spark.Spark.notFound;

public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    private RestExceptionHandler() {
    }

    public static void init() {
        handleDataNotFoundException();

        handleDataAccessException();

        handleCurrencyMismatchingException();

        handleOtherExceptions();

        notFound((req, res) -> {
            LOG.warn("Requested route not available: {} {}", req.requestMethod(), req.uri());
            res.status(BAD_REQUEST_400);
            return JsonUtils.toJson(new ApiError(BAD_REQUEST_400, getCode(BAD_REQUEST_400).getMessage()));
        });
    }

    private static void handleDataNotFoundException() {
        exception(DataNotFoundException.class, (ex, req, res) -> {
            LOG.error(ex.getMessage());
            res.status(NOT_FOUND_404);
            res.body(JsonUtils.toJson(new ApiError(NOT_FOUND_404, ex.getMessage())));
        });
    }

    private static void handleDataAccessException() {
        exception(DataAccessException.class, (ex, req, res) -> {
            LOG.error(ex.getMessage());
            res.status(BAD_REQUEST_400);
            res.body(JsonUtils.toJson(new ApiError(BAD_REQUEST_400, getCode(BAD_REQUEST_400).getMessage())));
        });
    }

    private static void handleCurrencyMismatchingException() {
        exception(CurrencyMismatchingException.class, (ex, req, res) -> {
            LOG.error(ex.getMessage());
            res.status(CONFLICT_409);
            res.body(JsonUtils.toJson(new ApiError(CONFLICT_409, ex.getMessage())));
        });
    }

    private static void handleOtherExceptions() {
        exception(Exception.class, (ex, req, res) -> {
            LOG.error(ex.getMessage());
            res.status(INTERNAL_SERVER_ERROR_500);
            res.body(JsonUtils.toJson(
                    new ApiError(INTERNAL_SERVER_ERROR_500, getCode(INTERNAL_SERVER_ERROR_500).getMessage())));
        });
    }
}
