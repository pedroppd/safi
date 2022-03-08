package br.com.cederj.safi.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "Wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalVolumeTrade;

    private BigDecimal tradingBalance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
