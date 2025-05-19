package com.viktor.cryptocurrency.trading.platform.web.controller;

import com.viktor.cryptocurrency.trading.platform.model.request.AuthenticationRequest;
import com.viktor.cryptocurrency.trading.platform.model.response.JwtTokenResponse;
import com.viktor.cryptocurrency.trading.platform.web.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> login(@RequestBody AuthenticationRequest details) {
        return ResponseEntity.ok().body(authenticationService.login(details));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody AuthenticationRequest details) {
        authenticationService.register(details);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
