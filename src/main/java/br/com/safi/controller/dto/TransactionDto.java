package br.com.safi.controller.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Builder
@Data
public class TransactionDto {

    private Long id;

    private Double currencyValue;

    private LocalDateTime transactionDate;

    private LocalDateTime createdAt;

    private String nameCurrency;

    private Double currencyQuantity;

    private String transactionName;

    private Long walletId;
}
