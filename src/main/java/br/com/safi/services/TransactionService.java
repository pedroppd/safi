package br.com.safi.services;

import br.com.safi.configuration.security.exception.dto.*;
import br.com.safi.controller.dto.TransactionDto;
import br.com.safi.controller.form.TransactionForm;
import br.com.safi.models.Currency;
import br.com.safi.models.Transaction;
import br.com.safi.models.User;
import br.com.safi.repository.ITransactionRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {


    private final CurrencyService currencyService;
    private final ITransactionRespository transactionRespository;
    private final WalletService walletService;
    private final TransactionStatusService transactionStatusService;
    private final WalletCurrencyService walletCurrencyService;
    private final UserService userService;

    @Async
    public CompletableFuture<Transaction> save(TransactionForm transactionForm) throws Exception {
        try {
            Currency currencies = this.getCurrency(transactionForm.getNameCurrency());
            Transaction transaction = transactionForm.converter(currencies, walletService, transactionStatusService);
            Transaction transactionSaved = transactionRespository.save(transaction);
            walletCurrencyService.save(transaction, this);
            return CompletableFuture.completedFuture(transactionSaved);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
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
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace(), "error", ex);
            throw new GetDataException(ex.getMessage());
        }
    }

    public List<Transaction> getTransactionByWalletId(Long id) throws GetDataException {
        try {
            return transactionRespository.getTransactionByWalletId(id);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new GetDataException(ex.getMessage());
        }
    }

    public List<Transaction> getTransactionByWalletIdAndYear(Long walletId, int year) throws GetDataException {
        try {
            return transactionRespository.getTransactionByWalletIdAndYear(walletId, year);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new GetDataException(ex.getMessage());
        }
    }

    public List<Transaction> getTransactionsByTransactionStatusAndCurrencyId(Long TransactionStatusId, Long CurrencyId) throws GetDataException {
        try {
            return transactionRespository.getTransactionByTransactionStatus_IdAndCurrency_Id(TransactionStatusId, CurrencyId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new GetDataException(ex.getMessage());
        }
    }

    public void deleteById(Long id) throws DataBaseException {
        try {
            transactionRespository.deleteById(id);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }

    public void deleteTransaction(Long transactionId, String userEmail) throws UnauthorizeException, UserNotFoundException {
        User user = userService.getUserByEmail(userEmail);
        Transaction transaction = transactionRespository.getTransactionById(transactionId).orElseThrow(() -> new TransactionNotFoundException(404, "Transação não encontrada"));
        if(transaction.getWallet().getUser().getId().equals(user.getId())) {
            transactionRespository.deleteById(transactionId);
        } else {
            String errorMessage = String.format("Usuário %n não está autorizado a deletar a transação com o id %s", user.getEmail(), transaction.getId());
            log.error(errorMessage);
            throw new UnauthorizeException(403, errorMessage);
        }
    }
}
