package com.viktor.cryptocurrency.trading.platform.web.service;

import com.viktor.cryptocurrency.trading.platform.config.exception.InvalidAmountException;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Crypto;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Transaction;
import com.viktor.cryptocurrency.trading.platform.model.domain.event.BuyTransactionRequestedEvent;
import com.viktor.cryptocurrency.trading.platform.model.domain.event.SellTransactionRequestedEvent;
import com.viktor.cryptocurrency.trading.platform.repository.CryptoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoService {
    private final CryptoRepository cryptoRepository;
    private final CryptoLookupService cryptoLookupService;
    private final ApplicationEventPublisher publisher;

    private final HashMap<String, String> symbolToName = new HashMap<>();

    public void saveCrypto(Crypto crypto) {
        resolveAndSetName(crypto);
        cryptoRepository.save(crypto);
    }

    public List<Crypto> getAllCryptos() {
        return cryptoRepository.getAll();
    }

    public Crypto getCryptoById(long id) {
        return cryptoRepository.findById(id);
    }

    public void buyCrypto(long userId, long cryptoId, BigDecimal amount) {
        validateAmount(amount);
        Crypto crypto = cryptoRepository.findById(cryptoId);
        publisher.publishEvent(new BuyTransactionRequestedEvent(new Transaction(
                userId,
                crypto.getCryptoId(),
                amount,
                crypto.getAsk(),
                Transaction.TransactionType.BUY
        )));
    }

    public void sellCrypto(long userId, long cryptoId, BigDecimal amount) {
        validateAmount(amount);
        Crypto crypto = cryptoRepository.findById(cryptoId);
        BigDecimal bid = crypto.getBid();
        publisher.publishEvent(new SellTransactionRequestedEvent(new Transaction(
                userId,
                crypto.getCryptoId(),
                amount,
                bid,
                Transaction.TransactionType.SELL
        )));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException();
        }
    }

    private void resolveAndSetName(Crypto crypto) {
        String symbol = crypto.getSymbol();
        if (symbolToName.containsKey(symbol)) {
            crypto.setName(symbolToName.get(symbol));
        } else {
            cryptoLookupService.getCryptoNameBySymbol(symbol).subscribe(name -> {
                crypto.setName(name);
                symbolToName.put(symbol, name);
            });
        }
    }
}
