package com.viktor.cryptocurrency.trading.platform.client.kraken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetryHandler {
    private static final Logger logger = LoggerFactory.getLogger(RetryHandler.class);

    private static final int MAX_RETRIES = 5;
    private static final long RETRY_INTERVAL_MS = 5000;

    public static void retry(Runnable operation) throws Exception {
        int attempt = 1;
        while (attempt <= MAX_RETRIES) {
            try {
                operation.run();
                return;
            } catch (Exception e) {
                logger.error("Attempt {} failed: {}", attempt, e.getMessage());
                if (attempt == MAX_RETRIES) {
                    throw e;
                } else {
                    attempt++;
                    Thread.sleep(RETRY_INTERVAL_MS);
                }
            }
        }
    }
}
