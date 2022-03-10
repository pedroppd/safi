package br.com.cederj.safi.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet_currency")
@Data
public class WalletCurrency {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Currency currency;

    private BigDecimal quantity;

    private BigDecimal averagePrice;

}
