package com.microservices.walletcrypto.mapper;

import com.microservices.walletcrypto.dto.WalletCryptoDto;
import com.microservices.walletcrypto.entity.WalletCrypto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WalletCryptoMapper {
    public WalletCryptoDto mapToWalletCryptoDto(WalletCrypto walletCrypto) {
        return WalletCryptoDto.builder()
                .id(walletCrypto.getId())
                .quantity(walletCrypto.getQuantity())
                .symbol(walletCrypto.getSymbol())
                .userId(walletCrypto.getUserId())
                .build();
    }

    public List<WalletCryptoDto> mapToWalletCryptoDtoList(List<WalletCrypto> walletCryptoList) {
        return walletCryptoList.stream()
                .map(this::mapToWalletCryptoDto)
                .collect(Collectors.toList());
    }
}
