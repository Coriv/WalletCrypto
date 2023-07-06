package com.microservices.walletcrypto.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TradeDto {

    private Long id;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal value;
    private boolean open;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    @NotNull
    private Long userId;
    @NotNull
    private String cryptoSymbol;
}