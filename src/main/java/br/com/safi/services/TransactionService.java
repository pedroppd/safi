package br.com.safi.services;

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
    private ICurrencyRepository currencyRepository;

    @Autowired
    private ITransactionRespository transactionRespository;

    @Autowired
    private WalletService walletService;

    @Async
    public CompletableFuture<Transaction> save(TransactionForm transactionForm) throws Exception {
        try {
            Map<String, Currency> currencies = this.handleCurrency(transactionForm).join();
            Transaction transaction = transactionForm.converter(currencies, walletService);
            return CompletableFuture.completedFuture(transactionRespository.save(transaction));
        }catch (Exception ex) {
            throw ex;
        }
    }

    @Async
    CompletableFuture<Map<String, Currency>> handleCurrency(TransactionForm transactionForm) {
        Currency inputCurrencyId = this.getCurrency(transactionForm.getInputCurrencyId(), transactionForm.getInputNameCurrency()).join();
        Currency outputCurrencyId = this.getCurrency(transactionForm.getOutputCurrencyId(), transactionForm.getOutputNameCurrency()).join();
        return CompletableFuture.completedFuture(Map.of("inputCurrencyId", inputCurrencyId, "outputCurrencyId", outputCurrencyId));
    }

    @Async
    CompletableFuture<Currency> getCurrency(Long id, String name) {
        if(id == null) {
            Currency newCurrency = this.createCurrencyByName(name.toUpperCase());
            Currency currency = currencyRepository.save(newCurrency);
            id = currency.getId();
        }
        return CompletableFuture.completedFuture(currencyRepository.getById(id));
    }

    private Currency createCurrencyByName(String name) {
        return Currency.builder().name(name).build();
    }
}
