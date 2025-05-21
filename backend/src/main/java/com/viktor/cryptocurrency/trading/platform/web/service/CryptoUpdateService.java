package com.viktor.cryptocurrency.trading.platform.web.service;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Crypto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoUpdateService {

    private final CryptoService cryptoService;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 5000)
    public void sendCryptoUpdates() {
        List<Crypto> updatedCryptos = cryptoService.getAllCryptos();
        messagingTemplate.convertAndSend("/topic/cryptos", updatedCryptos);
    }
}