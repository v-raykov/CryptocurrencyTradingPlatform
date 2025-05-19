package com.viktor.cryptocurrency.trading.platform.repository.util;

import com.viktor.cryptocurrency.trading.platform.config.exception.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcService {
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(JdbcService.class);

    public <T> List<T> queryForList(String sql, ResultSetMapper<T> mapper, Object... params) {
        List<T> results = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = prepareStatement(conn, sql, params);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(mapper.map(rs));
            }

        } catch (SQLException e) {
            logError(sql);
            throw new DatabaseException(e);
        }
        return results;
    }


    public <T> T queryForObject(String sql, ResultSetMapper<T> mapper, Object... params) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = prepareStatement(conn, sql, params);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) throw new RuntimeException("No results found");
            return mapper.map(rs);

        } catch (SQLException e) {
            logError(sql);
            throw new DatabaseException(e);
        }
    }

    public void executeUpdate(String sql, Object... params) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = prepareStatement(conn, sql, params)) {

            ps.executeUpdate();

        } catch (SQLException e) {
            logError(sql);
            throw new DatabaseException(e);
        }
    }

    private PreparedStatement prepareStatement(Connection conn, String sql, Object[] params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps;
    }

    @FunctionalInterface
    public interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    private void logError(String message) {
        logger.error("Failed to execute query {}", message);
    }
}
