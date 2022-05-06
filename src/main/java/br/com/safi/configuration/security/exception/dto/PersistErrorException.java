package br.com.safi.configuration.security.exception.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PersistErrorException extends Exception {

    public PersistErrorException(String message) {
        super(message);
    }
}
