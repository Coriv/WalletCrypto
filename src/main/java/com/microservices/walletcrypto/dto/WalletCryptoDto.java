package com.microservices.walletcrypto.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class WalletCryptoDto {

    private Long id;
    @PositiveOrZero
    private BigDecimal quantity;
    @NotNull
    private String symbol;
    @NotNull
    private Long userId;
}
