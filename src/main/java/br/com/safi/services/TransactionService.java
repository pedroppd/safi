package br.com.safi.services;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.configuration.security.exception.dto.PersistDataException;
import br.com.safi.controller.form.TransactionForm;
import br.com.safi.models.Currency;
import br.com.safi.models.Transaction;
import br.com.safi.repository.ICurrencyRepository;
import br.com.safi.repository.ITransactionRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class TransactionService {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ITransactionRespository transactionRespository;

    @Autowired
    private WalletService walletService;

    @Async
    public CompletableFuture<Transaction> save(TransactionForm transactionForm) throws Exception {
        try {
            Map<String, Currency> currencies = this.handleCurrency(transactionForm);
            Transaction transaction = transactionForm.converter(currencies, walletService);
            Transaction transactionSaved = transactionRespository.save(transaction);
            return CompletableFuture.completedFuture(transactionSaved);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }

    private Map<String, Currency> handleCurrency(TransactionForm transactionForm) throws DataBaseException, PersistDataException {
        Currency inputCurrencyId = this.getCurrency(transactionForm.getInputCurrencyId(), transactionForm.getInputNameCurrency());
        Currency outputCurrencyId = this.getCurrency(transactionForm.getOutputCurrencyId(), transactionForm.getOutputNameCurrency());
        return Map.of("inputCurrencyId", inputCurrencyId, "outputCurrencyId", outputCurrencyId);
    }


   private Currency getCurrency(Long id, String name) throws PersistDataException, DataBaseException {
       if (id == null) {
           Currency currencyExist = currencyService.getByName(name);
           if(currencyExist == null) {
               Currency currencyCreated = new Currency(name);
               return currencyService.save(currencyCreated);
           }
           return currencyExist;
       }
       return currencyService.getById(id);
   }
}
