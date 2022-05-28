package br.com.safi.controller.form;

import lombok.Getter;
import javax.validation.constraints.NotNull;

@Getter
public class RecoverPasswordForm {
    @NotNull(message = "Código de validação é obrigatório !")
    private String code;
    @NotNull(message = "Senha é obrigatória")
    private String password;
}
