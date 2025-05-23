package com.viktor.cryptocurrency.trading.platform.repository;

import com.viktor.cryptocurrency.trading.platform.config.exception.DatabaseException;
import com.viktor.cryptocurrency.trading.platform.config.exception.UserAlreadyExistsException;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import com.viktor.cryptocurrency.trading.platform.repository.util.*;
import com.viktor.cryptocurrency.trading.platform.repository.util.queries.UserQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcService jdbcService;

    public User findByUsername(String username) {
        try {
            return jdbcService.queryForObject(
                    UserQueries.FIND_USER_BY_USERNAME.getQuery(),
                    UserQueries::map,
                    username
            );
        } catch (Exception e) {
            throw new UsernameNotFoundException("%s not found".formatted(username));
        }
    }

    public void save(User user) {
        try {
            jdbcService.executeUpdate(
                    UserQueries.SAVE_USER.getQuery(),
                    user.getUsername(),
                    user.getPassword()
            );
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new UserAlreadyExistsException(user.getUsername());
            }
        }
    }

    public void resetUserById(long id) {
        jdbcService.executeUpdate(UserQueries.RESET_USER_BY_ID.getQuery(), id);
    }

    public void updateUserBalanceById(long id, BigDecimal amount) {
        jdbcService.executeUpdate(UserQueries.UPDATE_USER_BALANCE_BY_ID.getQuery(), amount, id);
    }

    public BigDecimal findUserBalanceById(long userId) {
        return jdbcService.queryForObject(
                UserQueries.FIND_USER_BALANCE_BY_ID.getQuery(),
                UserQueries::mapBalance,
                userId
        );
    }
}
