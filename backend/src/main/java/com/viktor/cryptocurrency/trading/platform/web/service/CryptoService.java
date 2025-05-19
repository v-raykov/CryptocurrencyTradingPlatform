package com.viktor.cryptocurrency.trading.platform.web.service;

import com.viktor.cryptocurrency.trading.platform.config.exception.InsufficientFundsException;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Crypto;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Transaction;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import com.viktor.cryptocurrency.trading.platform.model.domain.event.CryptoTransactionEvent;
import com.viktor.cryptocurrency.trading.platform.repository.CryptoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoService {
    private final CryptoRepository cryptoRepository;
    private final ApplicationEventPublisher publisher;

    public void saveCrypto(Crypto crypto) {
        cryptoRepository.save(crypto);
    }

    public List<Crypto> getAllCryptos() {
        return cryptoRepository.getAll();
    }

    public void buyCrypto(User user, String symbol, BigDecimal amount) {
        Crypto crypto = cryptoRepository.findCryptoBySymbol(symbol);
        BigDecimal bid = BigDecimal.valueOf(crypto.getBid());
        BigDecimal totalCost = bid.multiply(amount);

        validatePurchase(user.getBalance(), totalCost, amount);

        publisher.publishEvent(new CryptoTransactionEvent(new Transaction(
                user.getUserId(),
                crypto.getCryptoId(),
                amount,
                bid,
                Transaction.TransactionType.BUY
        )));
    }

    private void validatePurchase(BigDecimal balance, BigDecimal cost, BigDecimal amount) {
        if (balance.compareTo(cost) < 0) {
            throw new InsufficientFundsException(amount.toString());
        }
    }

}
