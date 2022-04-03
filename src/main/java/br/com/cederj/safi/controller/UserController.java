package br.com.cederj.safi.controller;

import br.com.cederj.safi.models.User;
import br.com.cederj.safi.models.form.UserForm;
import br.com.cederj.safi.repository.IUserRepository;
import br.com.cederj.safi.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<UserForm> register(@RequestBody @Valid UserForm userForm) {
        try {
            log.debug("Saving user . . .", userForm);
            User user = userForm.converter();
            userService.register(user);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }
}
