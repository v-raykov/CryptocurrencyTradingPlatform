package com.viktor.cryptocurrency.trading.platform.web.service;

import com.viktor.cryptocurrency.trading.platform.model.domain.Crypto;
import com.viktor.cryptocurrency.trading.platform.repository.CryptoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoService {
    private final CryptoRepository cryptoRepository;

    public void saveCrypto(Crypto crypto) {
        cryptoRepository.save(crypto);
    }

    public List<Crypto> getAllCryptos() {
        return cryptoRepository.getAll();
    }
}
