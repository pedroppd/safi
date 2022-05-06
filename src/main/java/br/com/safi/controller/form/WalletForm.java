package br.com.safi.controller.form;

import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.models.User;
import br.com.safi.models.Wallet;
import br.com.safi.services.UserService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletForm {

    private String name;
    private Long userId;

    public Wallet converter(UserService userService) throws GetDataException {
        try {
            User user = userService.getUserById(this.getUserId());
            return new Wallet(user, this.getName());
        } catch (Exception ex) {
            throw new GetDataException(ex.getMessage());
        }
    }
}
