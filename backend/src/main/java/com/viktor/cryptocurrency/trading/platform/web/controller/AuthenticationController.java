package com.viktor.cryptocurrency.trading.platform.web.controller;

import com.viktor.cryptocurrency.trading.platform.model.request.AuthenticationRequest;
import com.viktor.cryptocurrency.trading.platform.web.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService userService;

    @PostMapping("/login")
    public void login(@RequestBody AuthenticationRequest request) {
        userService.login(request);
    }
    @PostMapping("/register")
    public void register(@RequestBody AuthenticationRequest request) {
        userService.register(request);
    }
}
