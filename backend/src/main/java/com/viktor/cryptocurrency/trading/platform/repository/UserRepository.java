package com.viktor.cryptocurrency.trading.platform.repository;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import com.viktor.cryptocurrency.trading.platform.repository.util.*;
import com.viktor.cryptocurrency.trading.platform.repository.util.queries.UserQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcService jdbcService;

    public User findUserByUsername(String username) {
        try {
            return jdbcService.queryForObject(
                    UserQueries.GET_USER_BY_USERNAME.getQuery(),
                    UserQueries::map,
                    username
            );
        } catch (Exception e) {
            throw new UsernameNotFoundException("%s not found".formatted(username));
        }

    }

    public void save(User user) {
        jdbcService.executeUpdate(
                UserQueries.SAVE_USER.getQuery(),
                user.getUsername(),
                user.getPassword()
        );
    }

    public void resetUserBalance(User user) {
        jdbcService.executeUpdate(UserQueries.RESET_USER_BALANCE.getQuery(), user.getUserId());
    }

    public void updateBalance(User user, BigDecimal amount) {
        jdbcService.executeUpdate(UserQueries.UPDATE_USER_BALANCE.getQuery(), amount, user.getUserId());
    }
}
