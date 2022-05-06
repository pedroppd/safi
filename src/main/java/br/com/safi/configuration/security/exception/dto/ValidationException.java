package br.com.safi.configuration.security.exception.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }
}
