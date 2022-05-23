package br.com.safi.controller;

import br.com.safi.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
