package br.com.safi.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {

    private String token;
    private String type;

    public TokenDto(String token, String type) {
        this.token = token;
        this.type = type;
    }
}
