package br.com.safi.controller.form;

import br.com.safi.models.AbstractConverter;
import br.com.safi.models.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserForm extends AbstractConverter<User> {

    @NotEmpty(message = "Field firstName is required.")
    private String firstName;

    @NotEmpty(message = "Field lastName is required.")
    private String lastName;

    @Email(message = "Field email is required.")
    private String email;

    @NotEmpty(message = "Field email is required.")
    private String password;

    public User converter() {
        return new User(this.firstName, this.lastName, this.email, this.password);
    }
}
