package br.com.safi.controller.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class TransactionDto {

    private BigDecimal inputValue;

    private BigDecimal outputValue;

    private LocalDateTime transactionDate;

    private String inputNameCurrency;

    private String outputNameCurrency;

    private Long walletId;
}
