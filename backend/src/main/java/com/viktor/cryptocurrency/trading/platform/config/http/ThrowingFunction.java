package com.viktor.cryptocurrency.trading.platform.config.http;

@FunctionalInterface
public interface ThrowingFunction<T, R> {
    R apply(T t) throws Exception;
}