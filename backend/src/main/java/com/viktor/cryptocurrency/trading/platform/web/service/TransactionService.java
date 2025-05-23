package com.viktor.cryptocurrency.trading.platform.web.service;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Transaction;
import com.viktor.cryptocurrency.trading.platform.model.domain.event.TransactionApprovedEvent;
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

    @EventListener(TransactionApprovedEvent.class)
    public void onTransaction(TransactionApprovedEvent event) {
        Transaction transaction = event.transaction();
        if (transaction.getTransactionType() == Transaction.TransactionType.SELL) {
            transaction.setProfitLoss(getProfitLoss(transaction));
        }
        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByUserId(long userId) {
        return transactionRepository.findByUserId(userId);
    }

    private BigDecimal getProfitLoss(Transaction transaction) {
        return transaction.getAmount().multiply(transaction.getPriceAtTransaction())
                .subtract(
                        transactionRepository.findAverageBuyPriceByUserAndCryptoId(transaction.getUserId(), transaction.getCryptoId()
                        ));
    }
}
