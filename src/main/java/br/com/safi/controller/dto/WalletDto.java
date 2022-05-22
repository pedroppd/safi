package br.com.safi.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WalletDto {

    private Long id;

    private String name;

    private Double totalValueTrade;

    private Double tradeBalancing;

    private Long userId;

    private LocalDateTime createdAt;
}
