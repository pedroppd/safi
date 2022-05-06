package br.com.safi.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WalletDto {

    private Long id;

    private String name;

    private BigDecimal totalValueTrade;

    private BigDecimal tradeBalancing;

    private Long userId;
}
