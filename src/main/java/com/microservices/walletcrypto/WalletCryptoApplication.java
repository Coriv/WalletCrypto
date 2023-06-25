package com.microservices.walletcrypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WalletCryptoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletCryptoApplication.class, args);
    }

}
