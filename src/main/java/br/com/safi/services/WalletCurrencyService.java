package br.com.safi.services;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.configuration.security.exception.dto.ValidationException;
import br.com.safi.models.Transaction;
import br.com.safi.models.WalletCurrency;
import br.com.safi.repository.IWalletCurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class WalletCurrencyService {

    @Autowired
    private IWalletCurrencyRepository walletCurrencyRepository;
    private static final String BUY = "BUY";
    private static final String SELL = "SELL";

    public void save(Transaction transaction, TransactionService transactionService) throws GetDataException, ValidationException, DataBaseException {
        WalletCurrency walletCurrency = this.getWalletCurrencyByWalletIdAndCurrencyId(transaction.getWallet().getId(), transaction.getCurrency().getId());

        if (walletCurrency == null && SELL.equals(transaction.getTransactionStatus().getStatus())) {
            transactionService.deleteById(transaction.getId());
            String errorMessage = String.format("User %s does not have crypto %s in order to carry out a sale operation.", transaction.getWallet().getUser().getFirstName(), transaction.getCurrency().getName());
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        if (walletCurrency == null) {
            WalletCurrency walletCurrencyInputValue = this.CreateWalletCurrency(transaction);
            walletCurrencyRepository.save(walletCurrencyInputValue);
        } else {
            WalletCurrency walletCurrencyQuantityUpdated = increaseOrDecreaseQuantity(walletCurrency, transaction, transactionService);
            if (BUY.equals(transaction.getTransactionStatus().getStatus()) && walletCurrencyQuantityUpdated != null) {
                calcAveragePrice(walletCurrencyQuantityUpdated, transaction, transactionService);
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

    @Transactional
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
                .averagePrice(transaction.getCurrencyValue())
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

    private void calcAveragePrice(WalletCurrency walletCurrency, Transaction transaction, TransactionService transactionService) throws GetDataException {
        List<Transaction> transactionList = transactionService.getTransactionsByTransactionStatusAndCurrencyId(transaction.getTransactionStatus().getId(), walletCurrency.getCurrency().getId());
        // 1 transação = 1 compra
        //1ª compra: 200 moedas a R$ 14 (200 x R$ 14) = 2.800
        //2ª compra: 300 moedas a R$ 15 (300 x R$ 15) = 4.500
        //Quantidade total de moedas: 500 (200 + 300). Agora vamos ao cálculo do custo médio.
        //(2.800 + 10) + (4.500 + 10) / 500 = R$ 14,64 (preço médio)
        //BigDecimal quantity = transactionList.stream().map(Transaction::getCurrencyQuantity).reduce(BigDecimal::add).get();
        Double quantity = walletCurrency.getQuantity();
        Double currencyTotalPrice = transactionList
                .stream()
                .map((x) -> x.getCurrencyQuantity() * x.getCurrencyValue()).reduce(Double::sum).get();

        Double rest = currencyTotalPrice / quantity;
        walletCurrency.setAveragePrice(rest);
        walletCurrencyRepository.save(walletCurrency);
    }
}
