package br.com.safi.controller;

import br.com.safi.configuration.security.exception.dto.PersistDataException;
import br.com.safi.controller.form.ForgotEmailForm;
import br.com.safi.controller.form.RecoverPasswordForm;
import br.com.safi.models.User;
import br.com.safi.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/email")
@Slf4j
public class MailController {
    private final UserService userService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestParam(value = "code") String code) {
        if (userService.verify(code)) {
            return ResponseEntity.ok().body("verify_success");
        } else {
            return ResponseEntity.badRequest().body("verify_fail");
        }
    }

    @PostMapping("/forgot")
    public ResponseEntity<Void> forgot(@RequestBody @Valid ForgotEmailForm forgotEmailForm) {
        User user = userService.getUserByEmail(forgotEmailForm.getEmail());
        if (user != null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/recover")
    public ResponseEntity<Void> recover(@RequestBody @Valid RecoverPasswordForm recoverPasswordForm) throws PersistDataException {
        User user = userService.getUserByVerificationCode(recoverPasswordForm.getCode());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        user.setPassword(recoverPasswordForm.getPassword());
        userService.encodePassword(user);
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }
}
