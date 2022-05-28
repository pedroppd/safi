package br.com.safi.controller.form;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class ForgotEmailForm {
    @NotEmpty(message = "Email obrigatório")
    private String email;
}
