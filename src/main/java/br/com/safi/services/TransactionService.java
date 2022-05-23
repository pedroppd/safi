package br.com.safi.services;

import br.com.safi.configuration.security.exception.dto.*;
import br.com.safi.controller.dto.TransactionDto;
import br.com.safi.controller.form.TransactionForm;
import br.com.safi.models.*;
import br.com.safi.repository.ITransactionRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
            walletCurrencyService.save(transaction, this, "ADD");
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

    public void deleteTransaction(Long transactionId, String userEmail) throws UnauthorizeException, UserNotFoundException, TransactionNotFoundException, GetDataException, ValidationException, DataBaseException {
        User user = userService.getUserByEmail(userEmail);
        if(user == null) {
            throw new UserNotFoundException(404, "Usuário não existe !!!");
        }
        Transaction transaction = transactionRespository.getTransactionById(transactionId).orElseThrow(() -> new TransactionNotFoundException(404, "Transação não encontrada"));
        if(transaction.getWallet().getUser().getId().equals(user.getId())) {
            WalletCurrency walletCurrency = walletCurrencyService.getWalletCurrencyByWalletIdAndCurrencyId(transaction.getWallet().getId(), transaction.getCurrency().getId());
            System.out.println("walletCurrency" + walletCurrency);
            walletCurrencyService.updateTransactionAndWalletCurrency(transaction, "DELETE", walletCurrency, this);
            transactionRespository.deleteById(transactionId);
        } else {
            String errorMessage = String.format("Usuário %n não está autorizado a deletar a transação com o id %s", user.getEmail(), transaction.getId());
            log.error(errorMessage);
            throw new UnauthorizeException(403, errorMessage);
        }
    }

    public Transaction update(Long transactionId, TransactionForm transactionForm) throws Exception {
        Transaction transaction = transactionRespository.getTransactionById(transactionId).orElseThrow(() -> new TransactionNotFoundException(404, "Transação não encontrada."));
        Transaction transactionUpdated = updateFields(transaction, transactionForm);
        WalletCurrency walletCurrency = walletCurrencyService.getWalletCurrencyByWalletIdAndCurrencyId(transactionUpdated.getWallet().getId(), transaction.getCurrency().getId());
        walletCurrencyService.updateTransactionAndWalletCurrency(transaction, "UPDATE", walletCurrency, this);
        return transactionRespository.save(transactionUpdated);
    }

    public Transaction getTransactionById(Long id) throws TransactionNotFoundException {
        return transactionRespository.getTransactionById(id).orElseThrow(() -> new TransactionNotFoundException(404, "Transação não encontrada !"));
    }

    private Transaction updateFields(Transaction transaction, TransactionForm transactionDto) throws GetDataException, PersistDataException {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        if(transactionDto.getTransactionDate() != null) {
            LocalDate dateUpdated = LocalDate.parse(transactionDto.getTransactionDate());
            transaction.setTransactionDate(dateUpdated);
        }
        if(transactionDto.getTransactionStatusId() != null) {
            var transactionStatus = this.transactionStatusService.getById(transactionDto.getTransactionStatusId());
            transaction.setTransactionStatus(transactionStatus);
        }
        if(transactionDto.getCurrencyQuantity() != null) {
            transaction.setCurrencyQuantity(transactionDto.getCurrencyQuantity());
        }
        if(transactionDto.getAmountInvested() != null) {
            transaction.setAmountInvested(transactionDto.getAmountInvested());
        }
        if(transactionDto.getNameCurrency() != null) {
            Currency currency = this.getCurrency(transactionDto.getNameCurrency());
            transaction.setCurrency(currency);
        }
        transaction.setUpdatedAt(now);
        return transaction;
    }
}
