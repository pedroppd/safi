package br.com.safi.configuration.security.exception.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DataBaseException extends Exception {

    public DataBaseException(String message) {
        super(message);
    }
}
