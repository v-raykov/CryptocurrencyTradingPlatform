package com.viktor.cryptocurrency.trading.platform.web.controller;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Portfolio;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Transaction;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import com.viktor.cryptocurrency.trading.platform.model.server.response.UserBalanceResponse;
import com.viktor.cryptocurrency.trading.platform.web.service.PortfolioService;
import com.viktor.cryptocurrency.trading.platform.web.service.TransactionService;
import com.viktor.cryptocurrency.trading.platform.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final TransactionService transactionService;
    private final PortfolioService portfolioService;
    private final UserService userService;

    @GetMapping("/transaction-history")
    public List<Transaction> getUserTransactions(@AuthenticationPrincipal User user) {
        return transactionService.getTransactionsByUserId(user.getUserId());
    }

    @GetMapping("/portfolio")
    public List<Portfolio> getUserPortfolio(@AuthenticationPrincipal User user) {
        return portfolioService.getPortfolioByUserId(user.getUserId());
    }

    @GetMapping("/balance")
    public UserBalanceResponse getUser(@AuthenticationPrincipal User user) {
        return userService.getUserBalanceById(user.getUserId());
    }

    @PutMapping("/balance/reset")
    public void resetUserBalanceById(@AuthenticationPrincipal User user) {
        userService.resetUserBalanceById(user.getUserId());
    }

    @PutMapping("/balance/update")
    public void updateUserBalanceById(@AuthenticationPrincipal User user, @RequestParam BigDecimal amount) {
        userService.updateUserBalanceById(user.getUserId(), amount);
    }
}
