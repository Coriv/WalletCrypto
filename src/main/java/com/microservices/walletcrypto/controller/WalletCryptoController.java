package com.microservices.walletcrypto.controller;

import com.microservices.walletcrypto.dto.WalletCryptoDto;
import com.microservices.walletcrypto.mapper.WalletCryptoMapper;
import com.microservices.walletcrypto.service.WalletCryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet-crypto")
public class WalletCryptoController {

    private final WalletCryptoService walletCryptoService;
    private final WalletCryptoMapper walletCryptoMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<List<WalletCryptoDto>> fetchAllWalletCryptoByUserId(
            @PathVariable("userId") Long userId) {
        var list = walletCryptoService.findAllByUserId(userId);
        var listDto = walletCryptoMapper.mapToWalletCryptoDtoList(list);
        return ResponseEntity.ok(listDto);
    }

    @PostMapping("/createCryptoWallets/{userId}")
    public ResponseEntity<Void> createCryptoWalletsForUser(@PathVariable Long userId) {
        walletCryptoService.createWalletsForUser(userId);
        return ResponseEntity.ok().build();
    }
}
