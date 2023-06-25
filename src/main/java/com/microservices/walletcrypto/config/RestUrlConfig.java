package com.microservices.walletcrypto.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class RestUrlConfig {

    @Value("${cryptocurrency.symbols.url}")
    private String cryptoSymbols;
}
