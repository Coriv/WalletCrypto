package com.microservices.walletcrypto.service;

import com.microservices.walletcrypto.config.RestUrlConfig;
import com.microservices.walletcrypto.entity.WalletCrypto;
import com.microservices.walletcrypto.repository.WalletCryptoDao;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletCryptoService {

    @LoadBalanced
    private final RestTemplate restTemplate;

    private final RestUrlConfig restUrlConfig;

    private final WalletCryptoDao walletCryptoDao;

    public List<WalletCrypto> findAllByUserId(Long userId) {
        return walletCryptoDao.findAllByUserId(userId);
    }

    public void createWalletsForUser(Long userId) {
        String[] symbols = restTemplate.getForObject(restUrlConfig.getCryptoSymbols(), String[].class);
        Arrays.stream(symbols)
                .map(s -> {
                    WalletCrypto walletCrypto = new WalletCrypto();
                    walletCrypto.setSymbol(s);
                    walletCrypto.setUserId(userId);
                    return walletCrypto;
                })
                .forEach(walletCryptoDao::save);
    }
}
