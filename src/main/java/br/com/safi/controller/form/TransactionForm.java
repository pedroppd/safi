package br.com.safi.controller.form;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.models.*;
import br.com.safi.repository.ITransactionStatusRepository;
import br.com.safi.services.TransactionStatusService;
import br.com.safi.services.WalletService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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
    private Long transactionStatusId;

    public Transaction converter(Map<String, Currency> currencies, WalletService walletService, TransactionStatusService transactionStatusService) throws DataBaseException, GetDataException {
        Currency inputCurrency = currencies.get("inputCurrency");
        Currency outputCurrency = currencies.get("outputCurrency");
        Wallet wallet = walletService.getById(this.getWalletId());
        TransactionStatus transactionStatus = transactionStatusService.getById(this.getTransactionStatusId());

        if (wallet == null || transactionStatus == null) {
            String errorMessage = String.format("Invalid parameters walletId: %s transactionStatus: %s the values not exist in database", this.getWalletId(), this.getTransactionStatusId());
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
                .transactionStatus(transactionStatus)
                .build();
    }
}
