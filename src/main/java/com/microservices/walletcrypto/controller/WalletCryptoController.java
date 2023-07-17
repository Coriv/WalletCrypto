package com.microservices.walletcrypto.controller;

import com.microservices.walletcrypto.dto.CreateTradeDto;
import com.microservices.walletcrypto.dto.TransactionDto;
import com.microservices.walletcrypto.dto.WalletCryptoDto;
import com.microservices.walletcrypto.exception.FundsOnTheWalletException;
import com.microservices.walletcrypto.exception.NotEnoughFoundsException;
import com.microservices.walletcrypto.exception.WalletCryptoNotFoundException;
import com.microservices.walletcrypto.mapper.WalletCryptoMapper;
import com.microservices.walletcrypto.service.WalletCryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/wallet-crypto")
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

    @PostMapping("/createCryptoWallets")
    public ResponseEntity<Void> createCryptoWalletsForUser(@RequestParam("userId") Long userId) {
        walletCryptoService.createWalletsForUser(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deposit")
    public ResponseEntity<Void> depositCryptocurrency(
            @PathVariable Long userId,
            @RequestBody TransactionDto transactionDto) throws WalletCryptoNotFoundException {
        walletCryptoService.depositCrypto(transactionDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/check-trade")
    public ResponseEntity<Void> updateWalletScoreToTrade(@RequestBody CreateTradeDto createTradeDto) throws WalletCryptoNotFoundException, NotEnoughFoundsException {
        walletCryptoService.validateTradeAndUpdateWallet(createTradeDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteWalletCrypto(@RequestParam Long userId) throws FundsOnTheWalletException {
        walletCryptoService.deleteWalletsForUser(userId);
        return ResponseEntity.ok().build();
    }
}
