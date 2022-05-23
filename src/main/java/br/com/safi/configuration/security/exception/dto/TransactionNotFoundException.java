package br.com.safi.configuration.security.exception.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransactionNotFoundException extends Exception {

    private int statusCode;
    private String message;

    public TransactionNotFoundException(int statusCode, String message) {
       this.statusCode = statusCode;
       this.message = message;
    }
}
