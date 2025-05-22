package com.viktor.cryptocurrency.trading.platform.web.service;

import com.viktor.cryptocurrency.trading.platform.config.exception.InsufficientFundsException;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Transaction;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import com.viktor.cryptocurrency.trading.platform.model.domain.event.BuyTransactionRequestedEvent;
import com.viktor.cryptocurrency.trading.platform.model.domain.event.TransactionApprovedEvent;
import com.viktor.cryptocurrency.trading.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public void createUser(String username, String encodedPassword) {
        userRepository.save(new User(username, encodedPassword));
    }

    public BigDecimal getUserBalanceById(long id) {
        return userRepository.findUserBalanceById(id);
    }

    public void resetUserBalanceById(long id) {
        userRepository.resetUserBalanceById(id);
    }

    public void updateUserBalanceById(long id, BigDecimal amount) {
        userRepository.updateUserBalanceById(id, amount);
    }

    @EventListener
    public void onBuyCryptoEvent(BuyTransactionRequestedEvent event) {
        Transaction transaction = event.transaction();
        validatePurchase(
                userRepository.findUserBalanceById(transaction.getUserId()),
                transaction.getPriceAtTransaction(),
                transaction.getAmount()
        );
        publisher.publishEvent(new TransactionApprovedEvent(transaction));
    }

    private void validatePurchase(BigDecimal balance, BigDecimal ask, BigDecimal amount) {
        if (balance.compareTo(ask.multiply(amount)) < 0) {
            throw new InsufficientFundsException(amount.toString());
        }
    }
}
