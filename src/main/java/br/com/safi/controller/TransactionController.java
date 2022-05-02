package br.com.safi.controller;

import br.com.safi.controller.form.TransactionForm;
import br.com.safi.models.Transaction;
import br.com.safi.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/transaction")
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TransactionForm transactionForm, UriComponentsBuilder uriBuilder) {
        try {
            Transaction transaction = transactionService.save(transactionForm).join();
            URI uri = uriBuilder.path("/transaction/{id}").buildAndExpand(transaction.getId()).toUri();
            return ResponseEntity.created(uri).body(transaction.converter());
        }catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
}
