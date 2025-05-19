package com.viktor.cryptocurrency.trading.platform.web.controller;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Portfolio;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Transaction;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import com.viktor.cryptocurrency.trading.platform.web.service.PortfolioService;
import com.viktor.cryptocurrency.trading.platform.web.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final TransactionService transactionService;
    private final PortfolioService portfolioService;

    @GetMapping("/balance")
    public Integer getUser(@AuthenticationPrincipal User user) {
        return user.getBalance().intValue();
    }

    @GetMapping("/transactions-history")
    public List<Transaction> getTransactions(@AuthenticationPrincipal User user) {
        return transactionService.getTransactionsByUser(user);
    }

    @GetMapping("/portfolio")
    public List<Portfolio> getPortfolio(@AuthenticationPrincipal User user) {
        return portfolioService.getPortfolioByUser(user);
    }
}
