package com.microservices.walletcrypto;

import com.microservices.walletcrypto.dto.CreateTradeDto;
import com.microservices.walletcrypto.dto.TransactionDto;
import com.microservices.walletcrypto.entity.WalletCrypto;
import com.microservices.walletcrypto.exception.FundsOnTheWalletException;
import com.microservices.walletcrypto.exception.NotEnoughFoundsException;
import com.microservices.walletcrypto.exception.WalletCryptoNotFoundException;
import com.microservices.walletcrypto.feign.CryptocurrencyClient;
import com.microservices.walletcrypto.repository.WalletCryptoDao;
import com.microservices.walletcrypto.service.WalletCryptoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTestSuite {

    @InjectMocks
    private WalletCryptoService service;
    @Mock
    private WalletCryptoDao repository;
    @Mock
    private CryptocurrencyClient client;

    @Test
    public void testFindAllByUserId() {
        // given
        Long userId = 1L;
        WalletCrypto walletCrypto1 = new WalletCrypto();
        walletCrypto1.setId(1L);
        walletCrypto1.setSymbol("BTC");
        walletCrypto1.setQuantity(BigDecimal.valueOf(2));
        walletCrypto1.setUserId(userId);

        WalletCrypto walletCrypto2 = new WalletCrypto();
        walletCrypto2.setId(2L);
        walletCrypto2.setSymbol("ETH");
        walletCrypto2.setQuantity(BigDecimal.valueOf(5));
        walletCrypto2.setUserId(userId);

        List<WalletCrypto> expectedWalletCryptoList = new ArrayList<>();
        expectedWalletCryptoList.add(walletCrypto1);
        expectedWalletCryptoList.add(walletCrypto2);

        when(repository.findAllByUserId(userId)).thenReturn(expectedWalletCryptoList);
        // when
        List<WalletCrypto> result = service.findAllByUserId(userId);
        // then
        assertEquals(expectedWalletCryptoList, result);
    }

    @Test
    public void testCreateWalletsForUser() {
        // given
        Long userId = 1L;
        String[] symbols = {"BTC", "ETH"};
        when(client.getCryptoSymbols()).thenReturn(symbols);
        // when
        service.createWalletsForUser(userId);
        // then
        verify(repository, times(symbols.length)).save(any(WalletCrypto.class));
    }

    @Test
    public void testDepositCrypto() throws WalletCryptoNotFoundException {
        // given
        Long userId = 1L;
        String symbol = "BTC";
        BigDecimal currentQuantity = BigDecimal.valueOf(5);
        BigDecimal depositAmount = BigDecimal.valueOf(3);

        WalletCrypto walletCrypto = new WalletCrypto();
        walletCrypto.setId(1L);
        walletCrypto.setSymbol(symbol);
        walletCrypto.setQuantity(currentQuantity);
        walletCrypto.setUserId(userId);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setUserId(userId);
        transactionDto.setSymbol(symbol);
        transactionDto.setQuantity(depositAmount);

        when(repository.findByUserIdAndSymbol(userId, symbol)).thenReturn(Optional.of(walletCrypto));
        // when
        service.depositCrypto(transactionDto);
        // then
        assertEquals(currentQuantity.add(depositAmount), walletCrypto.getQuantity());
        verify(repository, times(1)).save(walletCrypto);
    }

    @Test
    public void testValidateTradeAndUpdateWallet() throws WalletCryptoNotFoundException, NotEnoughFoundsException {
        // given
        Long userId = 1L;
        String cryptoSymbol = "BTC";
        BigDecimal currentQuantity = BigDecimal.valueOf(5);
        BigDecimal tradeQuantity = BigDecimal.valueOf(3);

        WalletCrypto walletCrypto = new WalletCrypto();
        walletCrypto.setId(1L);
        walletCrypto.setSymbol(cryptoSymbol);
        walletCrypto.setQuantity(currentQuantity);
        walletCrypto.setUserId(userId);

        CreateTradeDto createTradeDto = new CreateTradeDto();
        createTradeDto.setUserId(userId);
        createTradeDto.setCryptoSymbol(cryptoSymbol);
        createTradeDto.setQuantity(tradeQuantity);

        when(repository.findByUserIdAndSymbol(userId, cryptoSymbol)).thenReturn(Optional.of(walletCrypto));
        // when
        service.validateTradeAndUpdateWallet(createTradeDto);
        // then
        assertEquals(currentQuantity.subtract(tradeQuantity), walletCrypto.getQuantity());
        verify(repository, times(1)).save(walletCrypto);
    }

    @Test
    public void testValidateTransactionInsufficientFunds() {
        // given
        BigDecimal currentQuantity = BigDecimal.valueOf(5);
        BigDecimal transactionQuantity = BigDecimal.valueOf(7);

        WalletCrypto walletCrypto = new WalletCrypto();
        walletCrypto.setId(1L);
        walletCrypto.setQuantity(currentQuantity);
        // when, then
        assertThrows(NotEnoughFoundsException.class, () -> service.validateTransaction(walletCrypto, transactionQuantity));
    }

    @Test
    public void throwWhenWalletsAreNotEmptyTest() {
        //give
        var userId = 3L;
        WalletCrypto walletCrypto = new WalletCrypto();
        walletCrypto.setId(1L);
        walletCrypto.setUserId(userId);
        walletCrypto.setQuantity(BigDecimal.TEN);

        WalletCrypto walletCrypto2 = new WalletCrypto();
        walletCrypto2.setId(2L);
        walletCrypto2.setUserId(userId);
        walletCrypto2.setQuantity(BigDecimal.ZERO);

        when(repository.findAllByUserId(userId)).thenReturn(List.of(walletCrypto, walletCrypto2));
        //when & then
        assertThrows(FundsOnTheWalletException.class, () -> service.deleteWalletsForUser(userId));
    }

    @Test
    public void doNotThrowWhenWalletAreEmptyTest() {
        //give
        var userId = 3L;
        WalletCrypto walletCrypto = new WalletCrypto();
        walletCrypto.setId(1L);
        walletCrypto.setUserId(userId);
        walletCrypto.setQuantity(BigDecimal.ZERO);

        WalletCrypto walletCrypto2 = new WalletCrypto();
        walletCrypto2.setId(2L);
        walletCrypto2.setUserId(userId);
        walletCrypto2.setQuantity(BigDecimal.ZERO);

        when(repository.findAllByUserId(userId)).thenReturn(List.of(walletCrypto, walletCrypto2));
        //when & then
        assertDoesNotThrow(() -> service.deleteWalletsForUser(userId));
    }

}
