package com.viktor.cryptocurrency.trading.platform.repository.util.queries;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Transaction;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public enum TransactionQueries {
    SAVE_TRANSACTION(String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?);", getTableName(), fields.USER_ID.name, fields.CRYPTO_ID.name, fields.AMOUNT.name, fields.PRICE_AT_TRANSACTION.name, fields.PROFIT_LOSS.name, fields.TRANSACTION_TYPE.name, fields.TRANSACTION_DATE)),
    FIND_BY_USER(String.format("SELECT * FROM %s WHERE %s = ? ORDER BY %s DESC", getTableName(), fields.USER_ID.name, fields.TRANSACTION_DATE.name)),
    FIND_AVERAGE_BUY_PRICE_BY_USER_AND_CRYPTO_ID("SELECT getAverageTransactionCost(?, ?) AS average_buy_price");

    private enum fields {
        TRANSACTION_ID("transaction_id"),
        USER_ID("user_id"),
        CRYPTO_ID("crypto_id"),
        AMOUNT("amount"),
        PRICE_AT_TRANSACTION("price_at_transaction"),
        PROFIT_LOSS("profit_loss"),
        TRANSACTION_TYPE("transaction_type"),
        TRANSACTION_DATE("transaction_date");

        private final String name;

        fields(String name) {
            this.name = name;
        }
    }

    private final String query;

    TransactionQueries(String query) {
        this.query = query;
    }

    public static Transaction map(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getLong(fields.TRANSACTION_ID.name),
                rs.getLong(fields.USER_ID.name),
                rs.getLong(fields.CRYPTO_ID.name),
                rs.getBigDecimal(fields.AMOUNT.name),
                rs.getBigDecimal(fields.PRICE_AT_TRANSACTION.name),
                rs.getBigDecimal(fields.PROFIT_LOSS.name),
                Transaction.TransactionType.valueOf(rs.getString(fields.TRANSACTION_TYPE.name)),
                rs.getString(fields.TRANSACTION_DATE.name)
        );
    }

    public static BigDecimal mapAverageBuyPrice(ResultSet rs) throws SQLException {
        return rs.getBigDecimal("average_buy_price");
    }

    private static String getTableName() {
        return "Transaction";
    }
}