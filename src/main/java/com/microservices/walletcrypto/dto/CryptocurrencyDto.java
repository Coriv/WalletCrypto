package com.microservices.walletcrypto.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CryptocurrencyDto {
    private final String symbol;
    private final String name;
}
