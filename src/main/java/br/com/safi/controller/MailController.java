package br.com.safi.controller;

import br.com.safi.models.User;
import br.com.safi.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/email")
@Slf4j
public class MailController {

    @Autowired
    private UserService userService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestParam(value = "code") String code) {
        if (userService.verify(code)) {
            return ResponseEntity.ok().body("verify_success");
        } else {
            return ResponseEntity.badRequest().body("verify_fail");
        }
    }

    @GetMapping("/{userId}/verify")
    public ResponseEntity<Boolean> userVerify(@PathVariable Long userId) {
        try {
            log.debug(String.format("Verifying user with id %n is enable", userId));
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(user.isEnabled());

        } catch (Exception e) {
            log.error(e.getMessage(), "stack", e.getStackTrace());
            throw e;
        }
    }
}
