package com.viktor.cryptocurrency.trading.platform.web.service;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Transaction;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import com.viktor.cryptocurrency.trading.platform.model.domain.event.CryptoTransactionEvent;
import com.viktor.cryptocurrency.trading.platform.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @EventListener(CryptoTransactionEvent.class)
    public void onTransaction(CryptoTransactionEvent event) {
        Transaction transaction = event.transaction();
        if (transaction.getTransactionType() == Transaction.TransactionType.SELL) {
            transaction.setProfitLoss(getProfitLoss(transaction));
        }
        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByUser(User user) {
        return transactionRepository.findByUser(user);
    }

    private BigDecimal getProfitLoss(Transaction transaction) {
        return transactionRepository.findAverageBuyPriceByUserAndCryptoId(transaction.getUserId(), transaction.getCryptoId()
                )
                .subtract(
                        transaction.getAmount().multiply(transaction.getPriceAtTransaction())
                );
    }
}
