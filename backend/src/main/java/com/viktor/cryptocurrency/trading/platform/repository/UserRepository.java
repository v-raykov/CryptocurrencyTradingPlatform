package com.viktor.cryptocurrency.trading.platform.repository;

import com.viktor.cryptocurrency.trading.platform.model.User;
import com.viktor.cryptocurrency.trading.platform.repository.util.*;
import com.viktor.cryptocurrency.trading.platform.repository.util.queries.UserQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcService jdbcService;

    public User getUserByUsername(String username) {
        return jdbcService.queryForObject(
                UserQueries.GET_USER_BY_USERNAME.getQuery(),
                UserQueries::map,
                username
        );
    }

    public void save(User user) {
        jdbcService.executeUpdate(
                UserQueries.SAVE_USER.getQuery(),
                user.getUsername(),
                user.getPassword()
        );
    }
}
