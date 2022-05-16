package br.com.safi.configuration.security.exception.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserNameNotFoundException extends Exception {

    public UserNameNotFoundException(String message) {
        super(message);
    }
}
