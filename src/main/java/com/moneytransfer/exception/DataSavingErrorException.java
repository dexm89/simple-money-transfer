package com.moneytransfer.exception;

public class DataSavingErrorException extends RuntimeException {

    public DataSavingErrorException(final String message) {
        super(message);
    }
}
