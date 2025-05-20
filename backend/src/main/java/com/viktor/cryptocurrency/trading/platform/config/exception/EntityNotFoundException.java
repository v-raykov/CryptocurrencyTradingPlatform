package com.viktor.cryptocurrency.trading.platform.config.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
        super("No results found for the given criteria.");
    }
}
