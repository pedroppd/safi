package br.com.safi.controller;

import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.controller.dto.TransactionDto;
import br.com.safi.controller.form.TransactionForm;
import br.com.safi.models.Currency;
import br.com.safi.models.Transaction;
import br.com.safi.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/transaction")
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TransactionForm transactionForm, UriComponentsBuilder uriBuilder) throws Exception {
        Transaction transaction = transactionService.save(transactionForm).join();
        URI uri = uriBuilder.path("/transaction/{id}").buildAndExpand(transaction.getId()).toUri();
        return ResponseEntity.created(uri).body(transaction.converter());
    }

    @GetMapping()
    public ResponseEntity<List<TransactionDto>> getAllTransactions() throws GetDataException {
        log.debug("Starting transaction end point...");
        List<TransactionDto> currencyList = transactionService.getAll();
        return ResponseEntity.ok().body(currencyList);
    }
}
