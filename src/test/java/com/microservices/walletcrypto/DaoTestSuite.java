package com.microservices.walletcrypto;

import com.microservices.walletcrypto.entity.WalletCrypto;
import com.microservices.walletcrypto.repository.WalletCryptoDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DaoTestSuite {
    @Autowired
    private WalletCryptoDao repository;

    @Test
    void saveNewWalletCryptoAndFindByIdTest() {
        //given
        WalletCrypto walletCrypto = new WalletCrypto();
        walletCrypto.setId(1L);
        walletCrypto.setUserId(3L);
        walletCrypto.setQuantity(BigDecimal.TEN);
        walletCrypto.setSymbol("BTC");
        //when
        repository.save(walletCrypto);
        WalletCrypto result = repository.findById(walletCrypto.getId())
                .orElseThrow();
        //then
        assertEquals(walletCrypto.getId(), result.getId());
        assertEquals(walletCrypto.getUserId(), result.getUserId());
        assertEquals(walletCrypto.getSymbol(), result.getSymbol());
        assertTrue(walletCrypto.getQuantity().compareTo(result.getQuantity()) == 0);
        //cleanUp
        repository.deleteById(result.getId());
    }
}
