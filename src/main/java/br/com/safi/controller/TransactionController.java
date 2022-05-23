package br.com.safi.controller;

import br.com.safi.configuration.security.exception.dto.*;
import br.com.safi.controller.dto.TransactionDto;
import br.com.safi.controller.form.TransactionForm;
import br.com.safi.models.Transaction;
import br.com.safi.models.WalletCurrency;
import br.com.safi.services.TransactionService;
import br.com.safi.services.WalletCurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/transaction")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid TransactionForm transactionForm, UriComponentsBuilder uriBuilder) throws Exception {
        Transaction transaction = transactionService.save(transactionForm).join();
        URI uri = uriBuilder.path("/transaction/{id}").buildAndExpand(transaction.getId()).toUri();
        return ResponseEntity.created(uri).body(transaction.converter());
    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsByWalletId(@PathVariable Long walletId) throws GetDataException {
        List<TransactionDto> transactionList = transactionService.getTransactionByWalletId(walletId).stream().map(Transaction::converter).collect(Collectors.toList());
        return ResponseEntity.ok().body(transactionList);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> getAllTransactionsByWalletId(@PathVariable Long transactionId, HttpServletRequest request) throws UnauthorizeException, UserNotFoundException, TransactionNotFoundException, ValidationException, GetDataException, DataBaseException {
        String userEmail = request.getRemoteUser();
        transactionService.deleteTransaction(transactionId, userEmail);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Transaction> getAllTransactionsByWalletId(@RequestBody TransactionForm transactionForm, @PathVariable Long transactionId, HttpServletRequest request) throws Exception {
        Transaction transaction = transactionService.update(transactionId, transactionForm);
        return ResponseEntity.ok().body(transaction);
    }
}
