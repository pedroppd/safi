package br.com.safi.controller.form;

import br.com.safi.models.User;
import br.com.safi.models.Wallet;
import br.com.safi.services.UserService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletForm {

    private Long userId;

    public Wallet converter(UserService userService) {
        try {
            User user = userService.getUserById(this.getUserId());
            return new Wallet(user);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
