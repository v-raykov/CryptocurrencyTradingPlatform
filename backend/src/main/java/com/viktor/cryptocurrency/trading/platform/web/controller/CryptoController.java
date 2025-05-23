package com.viktor.cryptocurrency.trading.platform.web.controller;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Crypto;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.User;
import com.viktor.cryptocurrency.trading.platform.web.service.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/crypto")
@RequiredArgsConstructor
public class CryptoController {
    private final CryptoService cryptoService;

    // WebSocket endpoint
    @MessageMapping("/crypto")
    @SendTo("/topic/cryptos")
    public ResponseEntity<List<Crypto>> fetchAllCryptosViaWebSocket() {
        return ResponseEntity.ok(cryptoService.getAllCryptos());
    }

    // REST endpoint
    @GetMapping
    public ResponseEntity<List<Crypto>> fetchAllCryptosViaRest() {
        return ResponseEntity.ok(cryptoService.getAllCryptos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Crypto> getCryptoById(@PathVariable long id) {
        return ResponseEntity.ok(cryptoService.getCryptoById(id));
    }

    @PostMapping("/{id}/buy")
    public ResponseEntity<Void> buyCrypto(@AuthenticationPrincipal User user, @PathVariable long id, @RequestParam BigDecimal amount) {
        cryptoService.buyCrypto(user.getUserId(), id, amount);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/sell")
    public ResponseEntity<Void> sellCrypto(@AuthenticationPrincipal User user, @PathVariable long id, @RequestParam BigDecimal amount) {
        cryptoService.sellCrypto(user.getUserId(), id, amount);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
