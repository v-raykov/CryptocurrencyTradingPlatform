package com.viktor.cryptocurrency.trading.platform.web.service;

import com.viktor.cryptocurrency.trading.platform.config.exception.InsufficientCryptoAmountException;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Portfolio;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Transaction;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import com.viktor.cryptocurrency.trading.platform.model.domain.event.CryptoTransactionEvent;
import com.viktor.cryptocurrency.trading.platform.model.domain.event.SellCryptoRequestedEvent;
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

    public List<Portfolio> getPortfolioByUser(User user) {
        return portfolioRepository.findByUserId(user);
    }

    @EventListener
    public void onSellCryptoRequestedEvent(SellCryptoRequestedEvent event) {
        Transaction transaction = event.transaction();
        Portfolio portfolio = portfolioRepository.findPortfolioByUserIdAndSymbol(transaction.getUserId(), transaction.getCryptoId());
        if (portfolio == null || portfolio.getAmount().compareTo(transaction.getAmount()) < 0) {
            throw new InsufficientCryptoAmountException();
        }
        publisher.publishEvent(new CryptoTransactionEvent(transaction));
    }
}
