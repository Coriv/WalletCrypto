package com.microservices.walletcrypto.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "CRYPTOCURRENCY")
public interface CryptocurrencyClient {

    @GetMapping(value = "v1/cryptocurrency/symbols")
    String[] getCryptoSymbols();
}
