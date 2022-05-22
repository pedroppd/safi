package br.com.safi.models;

import br.com.safi.controller.dto.TransactionDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transactions")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "currency_value")
    private Double currencyValue;

    @Column(name = "currency_quantity")
    private Double currencyQuantity;

    @OneToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "transactionStatus_id")
    private TransactionStatus transactionStatus;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "YYYY-MM-DD HH:MM:SS")
    private LocalDateTime createAt;

    public Transaction() {

    }

    public TransactionDto converter() {
        return TransactionDto.builder()
                .id(this.getId())
                .createdAt(this.getCreateAt())
                .transactionDate(this.getTransactionDate())
                .walletId(this.getWallet().getId())
                .nameCurrency(this.getCurrency().getName())
                .currencyValue(this.getCurrencyValue())
                .transactionName(this.getTransactionStatus().getStatus())
                .currencyQuantity(this.getCurrencyQuantity())
                .build();
    }

}
