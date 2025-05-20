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
@RequestMapping("/crypto")
@RequiredArgsConstructor
public class CryptoController {
    private final CryptoService cryptoService;

    @GetMapping
    public List<Crypto> getAllCryptos() {
        return cryptoService.getAllCryptos();
    }

    @GetMapping("/{id}")
    public Crypto getCryptoById(@PathVariable long id) {
        return cryptoService.getCryptoById(id);
    }

    @PostMapping("/{id}/buy")
    public void buyCrypto(@AuthenticationPrincipal User user, @PathVariable long id, @RequestParam BigDecimal amount) {
        cryptoService.buyCrypto(user, id, amount);
    }

    @PostMapping("/{id}/sell")
    public void sellCrypto(@AuthenticationPrincipal User user, @PathVariable long id, @RequestParam BigDecimal amount) {
        cryptoService.sellCrypto(user, id, amount);
    }
}
