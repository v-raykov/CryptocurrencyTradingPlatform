package com.viktor.cryptocurrency.trading.platform.repository;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Crypto;
import com.viktor.cryptocurrency.trading.platform.repository.util.JdbcService;
import com.viktor.cryptocurrency.trading.platform.repository.util.queries.CryptoQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CryptoRepository {
    private final JdbcService jdbcService;

    public List<Crypto> getAll() {
        return jdbcService.queryForList(
                CryptoQueries.GET_ALL_CRYPTOS.getQuery(),
                CryptoQueries::map
        );
    }

    public void save(Crypto crypto) {
        jdbcService.executeUpdate(
                CryptoQueries.SAVE_CRYPTO.getQuery(),
                crypto.getCryptoId(),
                crypto.getSymbol(),
                crypto.getBid(),
                crypto.getAsk(),
                crypto.getLast(),
                crypto.getVolume(),
                crypto.getLow(),
                crypto.getHigh());
    }

    public Crypto findCryptoBySymbol(String symbol) {
        return jdbcService.queryForObject(
                CryptoQueries.GET_CRYPTO_BY_SYMBOL.getQuery(),
                CryptoQueries::map,
                symbol);
    }
}
