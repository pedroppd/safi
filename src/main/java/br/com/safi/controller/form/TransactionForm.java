package br.com.safi.controller.form;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.models.*;
import br.com.safi.services.TransactionStatusService;
import br.com.safi.services.WalletService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import javax.validation.ValidationException;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Data
public class TransactionForm extends AbstractConverter<Transaction> {

    private Long transactionStatusId;
    private LocalDateTime transactionDate;
    @NotEmpty(message = "Input value is mandatory")
    private String nameCurrency;
    private BigDecimal currencyValue;
    private BigDecimal currencyQuantity;
    @NotEmpty(message = "WalletId is mandatory")
    private Long walletId;


    public Transaction converter(Currency currency, WalletService walletService, TransactionStatusService transactionStatusService) throws DataBaseException, GetDataException {
        Wallet wallet = walletService.getById(this.getWalletId());
        TransactionStatus transactionStatus = transactionStatusService.getById(this.getTransactionStatusId());
        if (wallet == null || transactionStatus == null) {
            String errorMessage = String.format("Invalid parameters walletId: %s transactionStatus: %s the values not exist in database", this.getWalletId(), this.getTransactionStatusId());
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return Transaction.builder()
                .createAt(now)
                .currencyQuantity(this.getCurrencyQuantity())
                .transactionDate(this.getTransactionDate())
                .currencyValue(this.getCurrencyValue())
                .wallet(wallet)
                .currency(currency)
                .transactionStatus(transactionStatus)
                .build();
    }
}
