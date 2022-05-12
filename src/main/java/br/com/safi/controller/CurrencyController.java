package br.com.safi.controller;

import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.models.Currency;
import br.com.safi.services.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping()
    public ResponseEntity<List<Currency>> getCurrencies() throws GetDataException {
      log.debug("Starting currency end point...");
      List<Currency> currencyList = currencyService.getAll();
      return ResponseEntity.ok().body(currencyList);
    }
}
