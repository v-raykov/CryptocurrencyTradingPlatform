package com.viktor.cryptocurrency.trading.platform.repository;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Portfolio;
import com.viktor.cryptocurrency.trading.platform.repository.util.JdbcService;
import com.viktor.cryptocurrency.trading.platform.repository.util.queries.PortfolioQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PortfolioRepository {
    private final JdbcService jdbcService;

    public List<Portfolio> findByUserId(long userId) {
        return jdbcService.queryForList(
                PortfolioQueries.FIND_BY_USER.getQuery(),
                PortfolioQueries::map,
                userId
        );
    }

    public Portfolio findByUserAndCryptoId(long userId, long cryptoId) {
        return jdbcService.queryForObject(
                PortfolioQueries.FIND_BY_USER_AND_CRYPTO_ID.getQuery(),
                PortfolioQueries::map,
                userId,
                cryptoId);
    }
}
