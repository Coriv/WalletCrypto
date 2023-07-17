package com.microservices.walletcrypto.repository;

import com.microservices.walletcrypto.dto.WalletCryptoDto;
import com.microservices.walletcrypto.entity.WalletCrypto;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface WalletCryptoDao extends CrudRepository<WalletCrypto, Long> {
    List<WalletCrypto> findAllByUserId(Long userId);
    Optional<WalletCrypto> findByUserIdAndSymbol(Long userId, String symbol);
    void deleteByUserId(Long userId);
}
