package br.com.safi.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserVerifyDto {
    private Boolean isEnable;
}
