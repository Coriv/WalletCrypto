package com.microservices.walletcrypto;

import com.microservices.walletcrypto.dto.WalletCryptoDto;
import com.microservices.walletcrypto.entity.WalletCrypto;
import com.microservices.walletcrypto.mapper.WalletCryptoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig
public class MapperTestSuite {

    private WalletCryptoMapper walletCryptoMapper = new WalletCryptoMapper();

    @Test
    public void testMapToWalletCryptoDto() {
        // given
        WalletCrypto walletCrypto = new WalletCrypto();
        walletCrypto.setId(1L);
        walletCrypto.setQuantity(BigDecimal.valueOf(5));
        walletCrypto.setSymbol("BTC");
        walletCrypto.setUserId(123L);
        // when
        WalletCryptoDto result = walletCryptoMapper.mapToWalletCryptoDto(walletCrypto);
        // then
        assertEquals(walletCrypto.getId(), result.getId());
        assertEquals(BigDecimal.valueOf(5), result.getQuantity());
        assertEquals(walletCrypto.getSymbol(), result.getSymbol());
        assertEquals(walletCrypto.getUserId(), result.getUserId());
    }

    @Test
    public void testMapToWalletCryptoDtoList() {
        // given
        List<WalletCrypto> walletCryptoList = new ArrayList<>();
        WalletCrypto walletCrypto1 = new WalletCrypto();
        walletCrypto1.setId(1L);
        walletCrypto1.setQuantity(BigDecimal.valueOf(5));
        walletCrypto1.setSymbol("BTC");
        walletCrypto1.setUserId(123L);
        walletCryptoList.add(walletCrypto1);

        WalletCrypto walletCrypto2 = new WalletCrypto();
        walletCrypto2.setId(2L);
        walletCrypto2.setQuantity(BigDecimal.valueOf(2));
        walletCrypto2.setSymbol("ETH");
        walletCrypto2.setUserId(123L);
        walletCryptoList.add(walletCrypto2);
        // when
        List<WalletCryptoDto> result = walletCryptoMapper.mapToWalletCryptoDtoList(walletCryptoList);
        // then
        assertEquals(2, result.size());
        assertEquals(walletCrypto1.getId(), result.get(0).getId());
        assertEquals(BigDecimal.valueOf(5), result.get(0).getQuantity());
        assertEquals(walletCrypto1.getSymbol(), result.get(0).getSymbol());
        assertEquals(walletCrypto1.getUserId(), result.get(0).getUserId());
        assertEquals(walletCrypto2.getId(), result.get(1).getId());
        assertEquals(BigDecimal.valueOf(2), result.get(1).getQuantity());
        assertEquals(walletCrypto2.getSymbol(), result.get(1).getSymbol());
        assertEquals(walletCrypto2.getUserId(), result.get(1).getUserId());
    }

}
