package br.com.safi.configuration.security.exception.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetDataException extends Exception {

    public GetDataException(String message) {
        super(message);
    }
}
