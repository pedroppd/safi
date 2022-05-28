package br.com.safi.controller.form;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.models.*;
import br.com.safi.services.TransactionStatusService;
import br.com.safi.services.WalletService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Required;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Data
public class TransactionForm extends AbstractConverter<Transaction> {

    @NotNull(message = "Status da transação é obrigatório")
    private Long transactionStatusId;
    @NotEmpty(message = "Data de transação é obrigatória")
    private String transactionDate;
    @NotEmpty(message = "Nome da moeda é obrigatório")
    private String nameCurrency;
    private Double amountInvested;
    @NotNull(message = "Quantidade da moeda é obrigatório")
    private Double currencyQuantity;
    @NotNull(message = "Id da carteira é obrigatório")
    private Long walletId;

    public Transaction converter(Currency currency, WalletService walletService, TransactionStatusService transactionStatusService) throws DataBaseException, GetDataException, ParseException {
        Wallet wallet = walletService.getById(this.getWalletId());
        TransactionStatus transactionStatus = transactionStatusService.getById(this.getTransactionStatusId());
        if (wallet == null || transactionStatus == null) {
            String errorMessage = String.format("Invalid parameters walletId: %s transactionStatus: %s the values not exist in database", this.getWalletId(), this.getTransactionStatusId());
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = myFormat.parse(this.getTransactionDate());
        var transactionDate = Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return Transaction.builder()
                .currencyQuantity(this.getCurrencyQuantity())
                .transactionDate(transactionDate)
                .amountInvested(this.getAmountInvested())
                .wallet(wallet)
                .currency(currency)
                .transactionStatus(transactionStatus)
                .createAt(now)
                .build();
    }
}
