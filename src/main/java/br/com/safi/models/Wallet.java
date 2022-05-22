package br.com.safi.models;

import br.com.safi.controller.WalletController;
import br.com.safi.controller.dto.WalletDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table(name = "Wallets")
@Getter
@Setter
@ToString
public class Wallet extends AbstractConverter<WalletDto> {

    public Wallet(User user, String name) {
        this.totalVolumeTrade = 0.0;
        this.tradingBalance = 0.0;
        this.user = user;
        this.name = name;
        this.setCreateAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    public Wallet() {
        this.setCreateAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Double totalVolumeTrade;

    private Double tradingBalance;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "YYYY-MM-DD HH:MM:SS")
    private LocalDateTime createAt;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "wallet")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private List<WalletCurrency> walletCurrency;

    @JsonIgnore
    @OneToMany(mappedBy = "wallet")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private List<Transaction> transactions;

    public WalletDto converter() {
        return WalletDto
                .builder()
                .id(this.getId())
                .createdAt(this.getCreateAt())
                .name(this.getName())
                .userId(this.getUser().getId())
                .totalValueTrade(this.getTotalVolumeTrade())
                .tradeBalancing(this.getTradingBalance())
                .build();
    }
}
