package br.com.safi.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
}
