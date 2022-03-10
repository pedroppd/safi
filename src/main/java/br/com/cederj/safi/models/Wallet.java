package br.com.cederj.safi.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
@Table(name = "Wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalVolumeTrade;

    private BigDecimal tradingBalance;

    @OneToMany(mappedBy = "wallet")
    private Set<WalletCurrency> registrations;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
