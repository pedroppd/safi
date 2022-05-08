package br.com.safi.controller.form;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.controller.dto.AbstractConverter;
import br.com.safi.models.Currency;
import br.com.safi.models.Transaction;
import br.com.safi.models.Wallet;
import br.com.safi.services.WalletService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ValidationException;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Data
public class TransactionForm extends AbstractConverter<Transaction> {

    @NotEmpty(message = "Input value is mandatory")
    private BigDecimal inputValue;
    @NotEmpty(message = "Output value is mandatory")
    private BigDecimal outputValue;
    @NotEmpty(message = "Transaction Date is mandatory")
    private LocalDateTime transactionDate;
    private String inputNameCurrency;
    private String outputNameCurrency;
    @NotEmpty(message = "WalletId is mandatory")
    private Long walletId;

    public Transaction converter(Map<String, Currency> currencies, WalletService walletService) throws DataBaseException {
        Currency inputCurrency = currencies.get("inputCurrency");
        Currency outputCurrency = currencies.get("outputCurrency");
        Long walletId = this.getWalletId();
        Wallet wallet = walletService.getById(walletId);
        if (wallet == null) {
            String errorMessage = String.format("Wallet with id %s not exist in database", this.getWalletId());
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        return Transaction.builder()
                .transactionDate(this.getTransactionDate())
                .inputCurrency(inputCurrency)
                .outputCurrency(outputCurrency)
                .inputValue(this.getInputValue())
                .outputValue(this.getOutputValue())
                .wallet(wallet)
                .build();
    }
}
