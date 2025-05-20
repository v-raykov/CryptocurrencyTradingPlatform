package com.viktor.cryptocurrency.trading.platform.repository.util.queries;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Portfolio;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public enum PortfolioQueries {
    FIND_BY_USER(String.format("SELECT * FROM %s WHERE %s = ?", getTableName(), fields.USER_ID.name)),
    FIND_BY_USER_AND_CRYPTO_ID(String.format("SELECT p.* FROM %s p WHERE p.%s = ? AND p.%s = ?",
            getTableName(), fields.USER_ID.name, fields.CRYPTO_ID.name));

    enum fields {
        PORTFOLIO_ID("portfolio_id"),
        USER_ID("user_id"),
        CRYPTO_ID("crypto_id"),
        AMOUNT("amount");
        private final String name;

        fields(String name) {
            this.name = name;
        }
    }

    private final String query;

    PortfolioQueries(String query) {
        this.query = query;
    }

    public static Portfolio map(ResultSet rs) throws SQLException {
        return new Portfolio(
                rs.getLong("portfolio_id"),
                rs.getLong("user_id"),
                rs.getLong("crypto_id"),
                rs.getBigDecimal("amount")
        );
    }

    private static String  getTableName() {
        return "Portfolio";
    }
}
