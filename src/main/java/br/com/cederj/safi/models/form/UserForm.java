package br.com.cederj.safi.models.form;

import br.com.cederj.safi.models.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserForm {

    @NotEmpty(message="Field firstName is required.")
    private String firstName;

    @NotEmpty(message="Field lastName is required.")
    private String lastName;

    @Email(message = "Field email is required.")
    private String email;

    @NotEmpty(message="Field email is required.")
    private String password;

    public User converter() {
        return new User(this.firstName, this.lastName, this.email, this.password);
    }
}
