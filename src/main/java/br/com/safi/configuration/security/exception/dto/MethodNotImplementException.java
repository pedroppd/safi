package br.com.safi.configuration.security.exception.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MethodNotImplementException extends Exception {

    public MethodNotImplementException(String message) {
        super(message);
    }
}
