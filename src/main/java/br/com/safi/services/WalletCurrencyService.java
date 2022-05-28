package br.com.safi.services;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.configuration.security.exception.dto.TransactionNotFoundException;
import br.com.safi.configuration.security.exception.dto.ValidationException;
import br.com.safi.models.Transaction;
import br.com.safi.models.WalletCurrency;
import br.com.safi.repository.IWalletCurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class WalletCurrencyService {

    @Autowired
    private IWalletCurrencyRepository walletCurrencyRepository;
    private static final String BUY = "COMPRA";
    private static final String SELL = "VENDA";
    private static final String ADD = "ADD";
    private static final String DELETE = "DELETE";
    private static final String UPDATE = "UPDATE";

    public void save(Transaction transaction, TransactionService transactionService, String mode) throws GetDataException, ValidationException, DataBaseException, TransactionNotFoundException {
        WalletCurrency walletCurrency = this.getWalletCurrencyByWalletIdAndCurrencyId(transaction.getWallet().getId(), transaction.getCurrency().getId());
        if (walletCurrency == null && SELL.equals(transaction.getTransactionStatus().getStatus())) {
            transactionService.deleteById(transaction.getId());
            String errorMessage = String.format("Usuário %s não possui cripto %s para realizar uma operação de venda.", transaction.getWallet().getUser().getFirstName(), transaction.getCurrency().getName());
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        if (walletCurrency == null) {
            WalletCurrency walletCurrencyInputValue = this.CreateWalletCurrency(transaction);
            walletCurrencyRepository.save(walletCurrencyInputValue);
        } else {
            updateTransactionAndWalletCurrency(transaction, mode, walletCurrency, transactionService);
        }
    }

    public void updateTransactionAndWalletCurrency(Transaction transaction, String mode, WalletCurrency walletCurrency, TransactionService transactionService) throws ValidationException, DataBaseException, GetDataException, TransactionNotFoundException {
        if (ADD.equals(mode)) {
            WalletCurrency walletCurrencyQuantityUpdated = increaseOrDecreaseQuantity(walletCurrency, transaction, transactionService);
            if (BUY.equals(transaction.getTransactionStatus().getStatus()) && walletCurrencyQuantityUpdated != null) {
                calcAveragePrice(walletCurrencyQuantityUpdated, transaction, transactionService, false);
            }
        } else if (DELETE.equals(mode)) {
            if (BUY.equals(transaction.getTransactionStatus().getStatus())) {
                walletCurrency.setQuantity(walletCurrency.getQuantity() - transaction.getCurrencyQuantity());
                if(walletCurrency.getQuantity() == 0) {
                    walletCurrencyRepository.deleteById(walletCurrency.getId());
                } else {
                    calcAveragePrice(walletCurrency, transaction, transactionService, true);
                }
            } else {
                walletCurrency.setQuantity(walletCurrency.getQuantity() + transaction.getCurrencyQuantity());
            }
        } else if (UPDATE.equals(mode)) {
            Transaction transactionSaved = transactionService.getTransactionById(transaction.getId());
            if (BUY.equals(transactionSaved.getTransactionStatus().getStatus())) {
                walletCurrency.setQuantity(walletCurrency.getQuantity() - transactionSaved.getCurrencyQuantity());
                calcAveragePrice(walletCurrency, transactionSaved, transactionService, true);
            } else {
                walletCurrency.setQuantity(walletCurrency.getQuantity() + transactionSaved.getCurrencyQuantity());
            }
            WalletCurrency walletCurrencyQuantityUpdated = increaseOrDecreaseQuantity(walletCurrency, transaction, transactionService);
            if (BUY.equals(transaction.getTransactionStatus().getStatus()) && walletCurrencyQuantityUpdated != null) {
                calcAveragePrice(walletCurrencyQuantityUpdated, transaction, transactionService, false);
            }
        }
    }

    private List<WalletCurrency> getWalletCurrencyByWalletId(Long id) throws GetDataException {
        try {
            return walletCurrencyRepository.getWalletCurrencyByWalletId(id);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new GetDataException(ex.getMessage());
        }
    }

    public WalletCurrency getWalletCurrencyByWalletIdAndCurrencyId(Long walletId, Long currencyId) throws GetDataException {
        try {
            return walletCurrencyRepository.getWalletCurrencyByWallet_IdAndCurrency_Id(walletId, currencyId);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new GetDataException(ex.getMessage());
        }
    }

    private WalletCurrency CreateWalletCurrency(Transaction transaction) {
        return WalletCurrency
                .builder()
                .currency(transaction.getCurrency())
                .wallet(transaction.getWallet())
                .quantity(transaction.getCurrencyQuantity())
                .averagePrice(transaction.getAmountInvested())
                .createAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private WalletCurrency increaseOrDecreaseQuantity(WalletCurrency walletCurrency, Transaction transaction, TransactionService transactionService) throws ValidationException, DataBaseException {
        if (BUY.equals(transaction.getTransactionStatus().getStatus())) {
            Double quantity = walletCurrency.getQuantity() + transaction.getCurrencyQuantity();
            walletCurrency.setQuantity(quantity);
            return walletCurrencyRepository.save(walletCurrency);
        } else {
            if (!transaction.getCurrency().getName().equals(walletCurrency.getCurrency().getName())) {
                transactionService.deleteById(transaction.getId());
                String errorMessage = String.format("User %s does not have crypto %s in order to carry out a sale operation.", walletCurrency.getWallet().getUser().getFirstName(), transaction.getCurrency().getName());
                log.error(errorMessage);
                throw new ValidationException(errorMessage);
            }
            double quantity = walletCurrency.getQuantity() - transaction.getCurrencyQuantity();
            if (quantity <= 0) {
                walletCurrencyRepository.deleteById(walletCurrency.getId());
            } else {
                walletCurrency.setQuantity(quantity);
                walletCurrencyRepository.save(walletCurrency);
            }
            return null;
        }
    }

    private void calcAveragePrice(WalletCurrency walletCurrency, Transaction transaction, TransactionService transactionService, boolean estorno) throws GetDataException {
        List<Transaction> transactionList = transactionService
                .getTransactionsByTransactionStatusAndCurrencyId(transaction.getTransactionStatus().getId(), walletCurrency.getCurrency().getId());

        if(estorno) {
            transactionList.remove(transaction);
        }

        // 1 transação = 1 compra
        //1ª compra: 200 moedas a R$ 14 (200 x R$ 14) = 2.800
        //2ª compra: 300 moedas a R$ 15 (300 x R$ 15) = 4.500
        //Quantidade total de moedas: 500 (200 + 300). Agora vamos ao cálculo do custo médio.
        //2.800 + 4.500 / 500 = R$ 14,64 (preço médio)
        //BigDecimal quantity = transactionList.stream().map(Transaction::getCurrencyQuantity).reduce(BigDecimal::add).get();

        Double quantity = walletCurrency.getQuantity();
        var currencyTotalPrice = transactionList
                .stream()
                .map((x) -> x.getCurrencyQuantity() * (x.getAmountInvested() / x.getCurrencyQuantity())).reduce(Double::sum);

        if(currencyTotalPrice.isPresent() && currencyTotalPrice.get() > 0 && quantity > 0) {
            Double rest = currencyTotalPrice.get() / quantity;
            System.out.println("REST:" + rest);
            walletCurrency.setAveragePrice(rest);
            walletCurrencyRepository.save(walletCurrency);
        } else {
            var valueTotal = currencyTotalPrice.isPresent() ? currencyTotalPrice.get() : 0;
            log.info("O cálculo do preço médio não foi realizado por que o valor total dos preços é igual a " +valueTotal+" e a quantidade é igual a " + quantity);
        }
    }
}
