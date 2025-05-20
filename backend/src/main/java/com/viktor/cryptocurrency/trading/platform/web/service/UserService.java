package com.viktor.cryptocurrency.trading.platform.web.service;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import com.viktor.cryptocurrency.trading.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public BigDecimal getUserBalance(User user) {
        return loadUserByUsername(user.getUsername()).getBalance();
    }

    public void resetUserBalance(User user) {
        userRepository.resetUserBalance(user);
    }

    public void updateBalance(User user, BigDecimal amount) {
        userRepository.updateBalance(user, amount);
    }
}
