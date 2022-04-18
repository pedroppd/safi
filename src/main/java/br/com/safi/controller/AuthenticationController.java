package br.com.safi.controller;

import br.com.safi.services.TokenService;
import br.com.safi.controller.dto.TokenDto;
import br.com.safi.controller.form.AuthForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@Slf4j()
@RestController()
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping()
    public ResponseEntity<TokenDto> authenticate(@RequestBody @Valid AuthForm authForm) {
        try {
            Authentication auth = authenticationManager.authenticate(authForm.converter());
            String token = tokenService.generateTokenJwt(auth);
            return ResponseEntity.ok().body(new TokenDto(token, "Bearer"));
        } catch(AuthenticationException ex) {
            log.error("Error to try authenticate:" +  ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
