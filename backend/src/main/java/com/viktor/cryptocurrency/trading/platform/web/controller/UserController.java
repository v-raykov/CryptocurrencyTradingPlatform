package com.viktor.cryptocurrency.trading.platform.web.controller;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Portfolio;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Transaction;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import com.viktor.cryptocurrency.trading.platform.model.server.response.UserBalanceResponse;
import com.viktor.cryptocurrency.trading.platform.web.service.PortfolioService;
import com.viktor.cryptocurrency.trading.platform.web.service.TransactionService;
import com.viktor.cryptocurrency.trading.platform.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Transaction>> getUserTransactions(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(transactionService.getTransactionsByUserId(user.getUserId()));
    }

    @GetMapping("/portfolio")
    public ResponseEntity<List<Portfolio>> getUserPortfolio(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(portfolioService.getPortfolioByUserId(user.getUserId()));
    }

    @GetMapping("/balance")
    public ResponseEntity<UserBalanceResponse> getUserBalance(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getUserBalanceById(user.getUserId()));
    }

    @PutMapping("/reset")
    public ResponseEntity<Void> resetUser(@AuthenticationPrincipal User user) {
        userService.resetUserById(user.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/balance/update")
    public ResponseEntity<Void> updateUserBalance(@AuthenticationPrincipal User user, @RequestParam BigDecimal amount) {
        userService.updateUserBalanceById(user.getUserId(), amount);
        return ResponseEntity.noContent().build();
    }
}
