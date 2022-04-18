package br.com.safi.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Setter
public class AuthForm {

    private String email;
    private String password;

    public UsernamePasswordAuthenticationToken converter(){
       return new UsernamePasswordAuthenticationToken(this.getEmail(), this.getPassword());
    }
}
