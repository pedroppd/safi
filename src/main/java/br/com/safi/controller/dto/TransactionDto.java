package br.com.safi.controller.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransactionDto {

    private Long id;

    private Double amountInvested;

    private String transactionDate;

    private String createdAt;

    private String nameCurrency;

    private Double currencyQuantity;

    private String transactionName;

    private Long walletId;
}
