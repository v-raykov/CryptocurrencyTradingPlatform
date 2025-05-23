package com.viktor.cryptocurrency.trading.platform.repository.util.queries;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Crypto;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum CryptoQueries {
    FIND_ALL_CRYPTOS(String.format("SELECT * FROM %s ORDER BY %s DESC LIMIT 20;", getTableName(), fields.ASK.name)),
    SAVE_CRYPTO(buildUpsertQuery()),
    FIND_CRYPTO_BY_ID(String.format("SELECT * FROM %s WHERE %s = ?;", getTableName(), fields.ID.name));

    private final String query;

    CryptoQueries(String query) {
        this.query = query;
    }

    @Getter
    public enum fields {
        ID("crypto_id"),
        NAME("name"),
        SYMBOL("symbol"),
        BID("bid"),
        ASK("ask"),
        LAST("last"),
        VOLUME("volume"),
        LOW("low"),
        HIGH("high");

        private final String name;

        fields(String name) {
            this.name = name;
        }
    }

    public static Crypto map(ResultSet rs) throws SQLException {
        return new Crypto(
                rs.getLong(fields.ID.name),
                rs.getString(fields.NAME.name),
                rs.getString(fields.SYMBOL.name),
                rs.getDouble(fields.BID.name),
                rs.getDouble(fields.ASK.name),
                rs.getDouble(fields.LAST.name),
                rs.getDouble(fields.VOLUME.name),
                rs.getDouble(fields.LOW.name),
                rs.getDouble(fields.HIGH.name));
    }

    private static String buildUpsertQuery() {
        List<fields> fieldsToInsert = getFieldsToInsert();
        return String.format(
                "INSERT INTO %s (%s) VALUES (%s) ON DUPLICATE KEY UPDATE %s;",
                getTableName(),
                buildColumns(fieldsToInsert),
                String.join(", ", Collections.nCopies(fieldsToInsert.size(), "?")),
                buildUpdateClauses(fieldsToInsert)
        );
    }

    private static List<fields> getFieldsToInsert() {
        return List.of(
                fields.NAME, fields.SYMBOL, fields.BID, fields.ASK, fields.LAST, fields.VOLUME, fields.LOW, fields.HIGH
        );
    }

    private static String buildColumns(List<fields> columns) {
        return columns.stream()
                .map(fields::getName)
                .collect(Collectors.joining(", "));
    }

    private static String buildUpdateClauses(List<fields> columns) {
        return columns.stream()
                .filter(f -> f != fields.SYMBOL)
                .map(f -> String.format("%s = VALUES(%s)", f.getName(), f.getName()))
                .collect(Collectors.joining(", "));
    }

    private static String getTableName() {
        return "Crypto";
    }

}
