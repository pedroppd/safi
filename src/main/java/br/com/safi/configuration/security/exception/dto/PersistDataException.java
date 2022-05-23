package br.com.safi.configuration.security.exception.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PersistDataException extends Exception {

    public PersistDataException(String message) {
        super(message);
    }
}
