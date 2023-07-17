package com.microservices.walletcrypto.exceptionHandler;

import com.microservices.walletcrypto.exception.FundsOnTheWalletException;
import com.microservices.walletcrypto.exception.NotEnoughFoundsException;
import com.microservices.walletcrypto.exception.WalletCryptoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WalletCryptoNotFoundException.class)
    public ResponseEntity<Object> walletCryptoNotFoundHandler(WalletCryptoNotFoundException e) {
        return new ResponseEntity<>("WalletCrypto with given ID doest not exist", NOT_FOUND);
    }

    @ExceptionHandler(NotEnoughFoundsException.class)
    public ResponseEntity<Object> notEnoughFoundsExceptionHandler(NotEnoughFoundsException e) {
        return new ResponseEntity<>("Not enough founds to process the transaction.", BAD_REQUEST);
    }

    @ExceptionHandler(FundsOnTheWalletException.class)
    public ResponseEntity<Object> fundsOnTheWalletExceptionHandler(FundsOnTheWalletException e) {
        return new ResponseEntity<>("You can not remove crypto wallet, because there are some founds.", BAD_REQUEST);
    }
}
