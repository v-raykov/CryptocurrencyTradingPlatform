package com.viktor.cryptocurrency.trading.platform.web.controller;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Crypto;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import com.viktor.cryptocurrency.trading.platform.web.service.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CryptoController {
    private final CryptoService cryptoService;

    @GetMapping("/crypto")
    public List<Crypto> getAllCryptos() {
        return cryptoService.getAllCryptos();
    }

    @PostMapping("/crypto/buy")
    public void buyCrypto(@AuthenticationPrincipal User user, @RequestParam String symbol, @RequestParam BigDecimal amount) {
        cryptoService.buyCrypto(user, symbol, amount);
    }

    @PostMapping("/crypto/sell")
    public void sellCrypto(@AuthenticationPrincipal User user, @RequestParam String symbol, @RequestParam BigDecimal amount) {
        cryptoService.sellCrypto(user, symbol, amount);
    }
}
