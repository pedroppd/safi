package br.com.safi.controller.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class TransactionDto {

    private Long id;

    private BigDecimal currencyValue;

    private LocalDateTime transactionDate;

    private LocalDateTime createdAt;

    private String nameCurrency;

    private BigDecimal currencyQuantity;

    private String transactionName;

    private Long walletId;
}
