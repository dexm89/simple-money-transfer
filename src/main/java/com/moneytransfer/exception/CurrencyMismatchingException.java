package com.moneytransfer.exception;

public class CurrencyMismatchingException extends RuntimeException {

    public CurrencyMismatchingException() {
        super("Source account currency differs of target");
    }
}
