package com.viktor.cryptocurrency.trading.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CryptocurrencyTradingPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(CryptocurrencyTradingPlatformApplication.class, args);
    }

}
