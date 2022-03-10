package br.com.cederj.safi.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transactions")
@Data
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
