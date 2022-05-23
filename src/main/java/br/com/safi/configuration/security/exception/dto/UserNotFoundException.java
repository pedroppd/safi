package br.com.safi.configuration.security.exception.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNotFoundException extends Exception {

    private int httpStatus;

    private String message;

    public UserNotFoundException(int httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
