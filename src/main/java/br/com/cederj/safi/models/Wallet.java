package br.com.cederj.safi.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "Wallets")
@NoArgsConstructor
@Getter
@Setter
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
