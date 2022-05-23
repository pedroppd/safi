package br.com.safi.controller.form;

import br.com.safi.configuration.security.exception.dto.UserNotFoundException;
import br.com.safi.models.AbstractConverter;
import br.com.safi.models.User;
import br.com.safi.models.Wallet;
import br.com.safi.services.UserService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletForm extends AbstractConverter<Wallet> {

    private String name;
    private Long userId;

    public Wallet converter(UserService userService) throws UserNotFoundException {
        User user = userService.getUserById(this.getUserId());
        if (user == null) {
            throw new UserNotFoundException(404, "User with id " + this.getUserId() + " Not Found !");
        }
        return new Wallet(user, this.getName());
    }
}
