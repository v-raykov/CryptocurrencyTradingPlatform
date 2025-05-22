package com.viktor.cryptocurrency.trading.platform.web.service;

import com.viktor.cryptocurrency.trading.platform.config.exception.InsufficientCryptoAmountException;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Portfolio;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Transaction;
import com.viktor.cryptocurrency.trading.platform.model.domain.event.TransactionApprovedEvent;
import com.viktor.cryptocurrency.trading.platform.model.domain.event.SellTransactionRequestedEvent;
import com.viktor.cryptocurrency.trading.platform.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final ApplicationEventPublisher publisher;

    public List<Portfolio> getPortfolioByUserId(long userId) {
        return portfolioRepository.findByUserId(userId);
    }

    @EventListener
    public void onSellCryptoRequestedEvent(SellTransactionRequestedEvent event) {
        Transaction transaction = event.transaction();
        validateAmount(transaction);
        publisher.publishEvent(new TransactionApprovedEvent(transaction));
    }

    private void validateAmount(Transaction transaction) {
        Portfolio portfolio = portfolioRepository.findByUserAndCryptoId(transaction.getUserId(), transaction.getCryptoId());
        if (portfolio == null || portfolio.getAmount().compareTo(transaction.getAmount()) < 0) {
            throw new InsufficientCryptoAmountException();
        }
    }
}
