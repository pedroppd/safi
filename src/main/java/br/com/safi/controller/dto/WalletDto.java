package br.com.safi.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class WalletDto {

    private Long id;

    private String name;

    private BigDecimal totalValueTrade;

    private BigDecimal tradeBalancing;

    private Long userId;

    private LocalDateTime createdAt;
}
