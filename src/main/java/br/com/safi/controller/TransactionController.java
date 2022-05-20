package br.com.safi.controller;

import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.controller.dto.TransactionDto;
import br.com.safi.controller.form.TransactionForm;
import br.com.safi.models.Transaction;
import br.com.safi.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsByWalletId(@PathVariable Long walletId) throws GetDataException {
        log.debug("Starting transaction end point...");
        List<TransactionDto> transactionList = transactionService.getTransactionByWalletId(walletId).stream().map(Transaction::converter).collect(Collectors.toList());
        return ResponseEntity.ok().body(transactionList);
    }
}
