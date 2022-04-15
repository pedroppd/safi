package br.com.safi.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transactions")
@NoArgsConstructor
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    private BigDecimal inputValue;

    private BigDecimal outputValue;

    @OneToOne
    @JoinColumn(name = "inputCurrency_id")
    private Currency inputCurrency;

    @OneToOne
    @JoinColumn(name = "outputCurrency_id")
    private Currency outputCurrency;

    private LocalDateTime transactionDate;

}
