package com.viktor.cryptocurrency.trading.platform.config.exception;

public class InsufficientCryptoAmountException extends RuntimeException {
    public InsufficientCryptoAmountException() {
        super("Insufficient crypto amount in portfolio.");
    }
}
