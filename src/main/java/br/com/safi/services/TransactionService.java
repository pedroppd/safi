package br.com.safi.services;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.configuration.security.exception.dto.PersistDataException;
import br.com.safi.controller.dto.TransactionDto;
import br.com.safi.controller.form.TransactionForm;
import br.com.safi.models.Currency;
import br.com.safi.models.Transaction;
import br.com.safi.repository.ITransactionRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionService {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ITransactionRespository transactionRespository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionStatusService transactionStatusService;

    @Async
    public CompletableFuture<Transaction> save(TransactionForm transactionForm) throws Exception {
        try {
            Map<String, Currency> currencies = this.handleCurrency(transactionForm);
            Transaction transaction = transactionForm.converter(currencies, walletService, transactionStatusService);
            Transaction transactionSaved = transactionRespository.save(transaction);
            return CompletableFuture.completedFuture(transactionSaved);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }

    private Map<String, Currency> handleCurrency(TransactionForm transactionForm) throws PersistDataException, GetDataException {
        Currency inputCurrency = this.getCurrency(transactionForm.getInputNameCurrency());
        Currency outputCurrency = this.getCurrency(transactionForm.getOutputNameCurrency());
        return Map.of("inputCurrency", inputCurrency, "outputCurrency", outputCurrency);
    }

    private Currency getCurrency(String name) throws GetDataException, PersistDataException {
        if (name == null) {
            log.error("The parameters name can't be null");
            throw new IllegalArgumentException("The parameters name can't be null");
        }
        Currency currencyExist = currencyService.getByName(name);
        if (currencyExist == null) {
            Currency currencyCreated = new Currency(name);
            return currencyService.save(currencyCreated);
        }
        return currencyExist;
    }

    public List<TransactionDto> getAll() throws GetDataException {
        try {
            return transactionRespository.findAll().stream().map(Transaction::converter).collect(Collectors.toList());
        }catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace(), "error", ex);
            throw new GetDataException(ex.getMessage());
        }
    }
}
