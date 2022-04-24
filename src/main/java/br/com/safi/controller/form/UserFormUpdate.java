package br.com.safi.controller.form;

import br.com.safi.models.User;
import br.com.safi.services.UserService;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserFormUpdate {

    @NotEmpty(message = "Field firstName is required.")
    private String firstName;

    @NotEmpty(message = "Field lastName is required.")
    private String lastName;

    @Email(message = "Field email is required.")
    private String email;


    public User update(long id, UserService userService) {
        User user = userService.getUserById(id);
        user.setPassword(this.getEmail());
        user.setEmail(this.getEmail());
        user.setLastName(this.getLastName());
        user.setFirstName(this.getFirstName());
        return user;
    }
}
