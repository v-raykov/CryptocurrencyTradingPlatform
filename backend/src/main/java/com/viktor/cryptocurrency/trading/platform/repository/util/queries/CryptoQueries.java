package com.viktor.cryptocurrency.trading.platform.repository.util.queries;

import com.viktor.cryptocurrency.trading.platform.model.domain.Crypto;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public enum CryptoQueries {
    GET_ALL_CRYPTOS("SELECT * FROM Crypto ORDER BY ask DESC LIMIT 20;"),
    SAVE_CRYPTO("INSERT INTO Crypto (symbol, bid, ask, last, volume, low, high)\n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "    bid = VALUES(bid),\n" +
            "    ask = VALUES(ask),\n" +
            "    last = VALUES(last),\n" +
            "    volume = VALUES(volume),\n" +
            "    low = VALUES(low),\n" +
            "    high = VALUES(high);");

    private final String query;

    CryptoQueries(String query) {
        this.query = query;
    }

    @Getter
    public enum fields {
        ID("crypto_id"),
        SYMBOL("symbol"),
        BID("bid"),
        ASK("ask"),
        LAST("last"),
        VOLUME("volume"),
        LOW("low"),
        HIGH("high");

        private final String field;

        fields(String field) {
            this.field = field;
        }
    }

    public static Crypto map(ResultSet rs) throws SQLException {
        return new Crypto(
                rs.getLong(fields.ID.field),
                rs.getString(fields.SYMBOL.field),
                rs.getDouble(fields.BID.field),
                rs.getDouble(fields.ASK.field),
                rs.getDouble(fields.LAST.field),
                rs.getDouble(fields.VOLUME.field),
                rs.getDouble(fields.LOW.field),
                rs.getDouble(fields.HIGH.field));
    }
}
