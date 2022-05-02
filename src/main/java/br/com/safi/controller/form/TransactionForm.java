package br.com.safi.controller.form;

import br.com.safi.models.Currency;
import br.com.safi.models.Transaction;
import br.com.safi.models.Wallet;
import br.com.safi.services.CurrencyService;
import br.com.safi.services.TransactionService;
import br.com.safi.services.WalletService;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class TransactionForm {

    @NotEmpty(message = "Input value is mandatory")
    private BigDecimal inputValue;
    @NotEmpty(message = "Output value is mandatory")
    private BigDecimal outputValue;
    @NotEmpty(message = "Transaction Date is mandatory")
    private LocalDateTime transactionDate;
    private Long inputCurrencyId;
    private String inputNameCurrency;
    private Long outputCurrencyId;
    private String outputNameCurrency;
    @NotEmpty(message = "WalletId is mandatory")
    private Long walletId;

    public Transaction converter(Map<String, Currency> currencies, WalletService walletService) throws Exception {
        Currency inputCurrency = currencies.get("inputCurrencyId");
        Currency outputCurrency = currencies.get("outputCurrencyId");
        Wallet wallet = walletService.getbyId(this.getWalletId());
        return Transaction.builder()
                .transactionDate(this.getTransactionDate())
                .inputCurrency(inputCurrency)
                .outputCurrency(outputCurrency)
                .inputValue(this.getInputValue())
                .outputValue(this.getOutputValue())
                .wallet(wallet).build();
    }
}
