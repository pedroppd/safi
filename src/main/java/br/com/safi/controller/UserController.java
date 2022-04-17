package br.com.safi.controller;

import br.com.safi.models.User;
import br.com.safi.models.dto.UserDto;
import br.com.safi.models.form.UserForm;
import br.com.safi.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserForm userForm, UriComponentsBuilder uriBuilder) throws MessagingException, IOException, ExecutionException, InterruptedException {
        String tid = UUID.randomUUID().toString();
        try {
            log.debug("Saving user...", "tid", tid, "user", userForm);
            User user = userForm.converter();
            System.out.println("THREAD 01 - " + Thread.currentThread().getName());
            User userSaved = userService.register(tid, user).join();
            URI uri = uriBuilder.path("/user/{uuid}").buildAndExpand(userSaved.getId()).toUri();
            System.out.println("THREAD 011 - " + Thread.currentThread().getName());
            return ResponseEntity.created(uri).body(userSaved.converter());
        } catch (Exception ex) {
            log.error(ex.getMessage(), "tid", tid, "stack", ex.getStackTrace(), "user", userForm);
            throw ex;
        }
    }
}
