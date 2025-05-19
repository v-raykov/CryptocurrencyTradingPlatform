package com.viktor.cryptocurrency.trading.platform.config.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super("Insufficient funds for amount: " + message);
    }
}
