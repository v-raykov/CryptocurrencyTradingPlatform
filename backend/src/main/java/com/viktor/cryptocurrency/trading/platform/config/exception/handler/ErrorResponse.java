package com.viktor.cryptocurrency.trading.platform.config.exception.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String timeStamp = LocalDateTime.now().toString();
    private final HttpStatus status;
    private final String message;
}

