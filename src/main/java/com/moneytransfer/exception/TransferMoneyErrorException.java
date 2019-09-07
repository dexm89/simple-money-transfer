package com.moneytransfer.exception;

import static java.lang.String.format;

public class TransferMoneyErrorException extends RuntimeException {

    public TransferMoneyErrorException(final int accIdToDebit, final int accIdToCredit) {
        super(format("Transfer money from %d to %d has been interrupted", accIdToDebit, accIdToCredit));
    }
}
