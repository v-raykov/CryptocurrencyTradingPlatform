package com.viktor.cryptocurrency.trading.platform.config.exception;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("Amount for transaction must be greater than zero.");
    }
}
