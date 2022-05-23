package br.com.safi.controller.dto;

import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
public class UserDto {
    private long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isEnable;
}
