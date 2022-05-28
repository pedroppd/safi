package br.com.safi.configuration.security.exception.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PersistDataException extends Exception {
    private int code;
    public PersistDataException(int code, String message) {
        super(message);
        this.code = code;
    }
}
