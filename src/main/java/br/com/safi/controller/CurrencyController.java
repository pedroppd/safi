package br.com.safi.controller;

import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.models.Currency;
import br.com.safi.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/{walletId}")
    public ResponseEntity<List<String>> getCurrencies(@PathVariable Long walletId) throws GetDataException {
        return ResponseEntity.ok().body(currencyService.getById(walletId));
    }
}
