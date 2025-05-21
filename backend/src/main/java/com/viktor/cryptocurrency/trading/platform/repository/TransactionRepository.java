package com.viktor.cryptocurrency.trading.platform.repository;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Transaction;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import com.viktor.cryptocurrency.trading.platform.repository.util.JdbcService;
import com.viktor.cryptocurrency.trading.platform.repository.util.queries.TransactionQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {
    private final JdbcService jdbcService;

    public void save(Transaction transaction) {
        jdbcService.executeUpdate(
                TransactionQueries.SAVE_TRANSACTION.getQuery(),
                transaction.getUserId(), transaction.getCryptoId(), transaction.getAmount(), transaction.getPriceAtTransaction(), transaction.getProfitLoss(), transaction.getTransactionType().name(), transaction.getTransactionDate()
        );
    }

    public List<Transaction> findByUser(User user) {
        return jdbcService.queryForList(
                TransactionQueries.FIND_BY_USER.getQuery(),
                TransactionQueries::map,
                user.getUserId()
        );
    }

    public BigDecimal findAverageBuyPriceByUserAndCryptoId(long userId, long cryptoId) {
        return jdbcService.queryForObject(
                TransactionQueries.FIND_AVERAGE_BUY_PRICE_BY_USER_AND_CRYPTO_ID.getQuery(),
                TransactionQueries::mapAverageBuyPrice,
                userId,
                cryptoId);
    }

}
