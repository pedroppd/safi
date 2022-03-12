package br.com.cederj.safi.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet_currency")
@NoArgsConstructor
@Getter
@Setter
public class WalletCurrency {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private BigDecimal quantity;

    private BigDecimal averagePrice;

}
