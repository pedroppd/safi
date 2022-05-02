package br.com.safi.models;

import br.com.safi.controller.dto.WalletDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "Wallets")
@Getter
@Setter
public class Wallet {

    public Wallet(User user) {
        this.totalVolumeTrade = BigDecimal.ZERO;
        this.tradingBalance = BigDecimal.ZERO;
        this.user = user;
    }

    public Wallet() {
    }

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

    public WalletDto converter() {
        return WalletDto
                .builder()
                .id(this.getId())
                .userId(this.getId())
                .totalValueTrade(this.getTotalVolumeTrade())
                .tradeBalancing(this.getTradingBalance())
                .build();
    }
}
