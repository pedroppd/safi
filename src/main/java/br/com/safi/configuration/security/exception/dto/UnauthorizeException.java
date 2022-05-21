package br.com.safi.configuration.security.exception.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UnauthorizeException extends Exception {

    private int httpStatus;
    private String errorMessage;

    public UnauthorizeException(int httpStatus, String errorMessage) {
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
