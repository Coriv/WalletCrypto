package com.microservices.walletcrypto.exceptionHandler;

import com.microservices.walletcrypto.exception.NotEnoughFoundsException;
import com.microservices.walletcrypto.exception.WalletCryptoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WalletCryptoNotFoundException.class)
    public ResponseEntity<Object> walletCryptoNotFoundHandler(WalletCryptoNotFoundException e) {
        return new ResponseEntity<>("WalletCrypto with given ID doest not exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotEnoughFoundsException.class)
    public ResponseEntity<Object> notEnoughFoundsExceptionHandler(NotEnoughFoundsException e) {
        return new ResponseEntity<>("Not enought founds to process the transaction.", HttpStatus.BAD_REQUEST);
    }
}
