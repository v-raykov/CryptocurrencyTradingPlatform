package com.viktor.cryptocurrency.trading.platform.config.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(Exception e) {
        super(e);
    }
}
