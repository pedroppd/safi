package br.com.safi.controller;

import br.com.safi.controller.dto.UserVerifyDto;
import br.com.safi.controller.form.UserFormUpdate;
import br.com.safi.models.User;
import br.com.safi.controller.dto.UserDto;
import br.com.safi.controller.form.UserForm;
import br.com.safi.repository.IUserRepository;
import br.com.safi.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private IUserRepository userRepository;

    @PostMapping()
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserForm userForm, UriComponentsBuilder uriBuilder) throws MessagingException, IOException, ExecutionException, InterruptedException {
        String tid = UUID.randomUUID().toString();
        try {
            log.debug("Saving user...", "tid", tid, "user", userForm);
            User user = userForm.converter();
            User userSaved = userService.register(tid, user).join();
            URI uri = uriBuilder.path("/user/{id}").buildAndExpand(userSaved.getId()).toUri();
            return ResponseEntity.created(uri).body(userSaved.converter());
        } catch (Exception ex) {
            log.error(ex.getMessage(), "tid", tid, "stack", ex.getStackTrace(), "user", userForm);
            throw ex;
        }
    }

    @GetMapping( "/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "id") long id) {
        String tid = UUID.randomUUID().toString();
        try {
            log.debug(String.format("Gettting user with id {}...", id), "tid", tid);
            User user = userService.getUserById(id);
            if(user != null) {
                return ResponseEntity.ok().body(user.converter());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error(ex.getMessage(), "tid", tid, "stack", ex.getStackTrace(), "userId", id);
            throw ex;
        }
    }

    @PutMapping( "/{id}")
    @Transactional
    public ResponseEntity<UserDto> updateUser(@PathVariable(value = "id") long id, @RequestBody UserFormUpdate userForm) {
        String tid = UUID.randomUUID().toString();
        try {
            log.info(String.format("Updating user with id %s .", id), "tid", tid);
            User user = userForm.update(id, userService);
            userRepository.save(user);
            return ResponseEntity.ok().body(user.converter());
        } catch (Exception ex) {
            log.error(ex.getMessage(), "tid", tid, "stack", ex.getStackTrace(), "userId", id);
            throw ex;
        }
    }

    @DeleteMapping( "/{id}")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") long id) {
        String tid = UUID.randomUUID().toString();
        try {
            log.info(String.format("Updating user with id %s .", id), "tid", tid);
            userService.deleteUserById(id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error(ex.getMessage(), "tid", tid, "stack", ex.getStackTrace(), "userId", id);
            throw ex;
        }
    }

    @GetMapping("/{userId}/verify")
    public ResponseEntity<UserVerifyDto> userVerify(@PathVariable Long userId) {
        try {
            log.debug(String.format("Verifying user with id %n is enable", userId));
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            UserVerifyDto userDto = UserVerifyDto.builder().isEnable(user.isEnabled()).build();

            return ResponseEntity.ok().body(userDto);

        } catch (Exception e) {
            log.error(e.getMessage(), "stack", e.getStackTrace());
            throw e;
        }
    }
}
