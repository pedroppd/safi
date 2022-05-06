package br.com.safi.controller;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.controller.dto.WalletDto;
import br.com.safi.controller.form.WalletForm;
import br.com.safi.models.Wallet;
import br.com.safi.services.UserService;
import br.com.safi.services.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<WalletDto> save(@RequestBody WalletForm walletForm, UriComponentsBuilder uriBuilder) throws DataBaseException {
        try {
            Wallet wallet = walletService.save(walletForm.converter(userService));
            URI uri = uriBuilder.path("/wallet/{id}").buildAndExpand(wallet.getId()).toUri();
            return ResponseEntity.created(uri).body(wallet.converter());
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }

    //Criar interceptor para verificar se o usuário que está deletando a conta tem permissão para tal ação.
    @DeleteMapping("/{walletId}")
    public ResponseEntity<WalletDto> delete(@PathVariable Long walletId) throws DataBaseException {
        try {
            walletService.delete(walletId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }
}
