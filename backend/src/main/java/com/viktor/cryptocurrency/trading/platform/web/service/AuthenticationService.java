package com.viktor.cryptocurrency.trading.platform.web.service;

import com.viktor.cryptocurrency.trading.platform.config.security.jwt.JwtUtils;
import com.viktor.cryptocurrency.trading.platform.model.server.request.AuthenticationRequest;
import com.viktor.cryptocurrency.trading.platform.model.server.response.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public void register(AuthenticationRequest request) {
        userService.createUser(request.username(), passwordEncoder.encode(request.password()));
    }

    public JwtTokenResponse login(AuthenticationRequest details) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(details.username(), details.password()));
        userService.loadUserByUsername(details.username());
        return new JwtTokenResponse(jwtUtils.generateToken(details.username()));
    }
}
