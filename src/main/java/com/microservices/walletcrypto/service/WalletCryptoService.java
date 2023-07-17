package com.microservices.walletcrypto.service;

import com.microservices.walletcrypto.dto.CreateTradeDto;
import com.microservices.walletcrypto.dto.TransactionDto;
import com.microservices.walletcrypto.entity.WalletCrypto;
import com.microservices.walletcrypto.exception.FundsOnTheWalletException;
import com.microservices.walletcrypto.exception.NotEnoughFoundsException;
import com.microservices.walletcrypto.exception.WalletCryptoNotFoundException;
import com.microservices.walletcrypto.feign.CryptocurrencyClient;
import com.microservices.walletcrypto.repository.WalletCryptoDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletCryptoService {
    private final WalletCryptoDao walletCryptoDao;
    private final CryptocurrencyClient client;

    public List<WalletCrypto> findAllByUserId(Long userId) {
        return walletCryptoDao.findAllByUserId(userId);
    }

    public void createWalletsForUser(Long userId) {
        String[] symbols = client.getCryptoSymbols();
        Arrays.stream(symbols)
                .map(s -> {
                    WalletCrypto walletCrypto = new WalletCrypto();
                    walletCrypto.setSymbol(s);
                    walletCrypto.setUserId(userId);
                    return walletCrypto;
                })
                .forEach(walletCryptoDao::save);
    }

    public void depositCrypto(TransactionDto transactionDto) throws WalletCryptoNotFoundException {
        long userId = transactionDto.getUserId();
        var walletCrypto = walletCryptoDao.findByUserIdAndSymbol(userId, transactionDto.getSymbol())
                .orElseThrow(WalletCryptoNotFoundException::new);
        walletCrypto.setQuantity(walletCrypto.getQuantity().add(transactionDto.getQuantity()));
        walletCryptoDao.save(walletCrypto);
    }

    public void validateTradeAndUpdateWallet(CreateTradeDto createTradeDto) throws WalletCryptoNotFoundException, NotEnoughFoundsException {
        var userId = createTradeDto.getUserId();
        var cryptoId = createTradeDto.getCryptoSymbol();
        var quantity = createTradeDto.getQuantity();
        var walletCrypto = walletCryptoDao.findByUserIdAndSymbol(userId, cryptoId).orElseThrow(WalletCryptoNotFoundException::new);
        validateTransaction(walletCrypto, quantity);
        updateWallet(walletCrypto, quantity);
    }

    public void validateTransaction(WalletCrypto walletCrypto, BigDecimal quantity) throws NotEnoughFoundsException {
        if (walletCrypto.getQuantity().compareTo(quantity) < 0)
            throw new NotEnoughFoundsException();
    }

    private void updateWallet(WalletCrypto walletCrypto, BigDecimal quantity) {
        walletCrypto.setQuantity(walletCrypto.getQuantity().subtract(quantity));
        walletCryptoDao.save(walletCrypto);
    }

    public void deleteWalletsForUser(Long userId) throws FundsOnTheWalletException {
        areThereFundsInTheAccount(userId);
        walletCryptoDao.deleteById(userId);
    }

    private void areThereFundsInTheAccount(Long userId) throws FundsOnTheWalletException {
        boolean walletsAreNotEmpty = walletCryptoDao.findAllByUserId(userId).stream()
                .anyMatch(this::checkQuantityNotEqualsToZero);
        if(walletsAreNotEmpty) {
            throw new FundsOnTheWalletException();
        }
    }
    private boolean checkQuantityNotEqualsToZero(WalletCrypto wallet) {
        return !wallet.getQuantity().equals(BigDecimal.ZERO);
    }
 }
