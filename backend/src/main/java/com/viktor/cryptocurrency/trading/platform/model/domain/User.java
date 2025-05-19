package com.viktor.cryptocurrency.trading.platform.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class User implements UserDetails {
    private Long userId;
    private String username;
    private String password;
    private BigDecimal balance;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of((GrantedAuthority) () -> "USER");
    }
}
