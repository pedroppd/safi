package br.com.safi.models;

import br.com.safi.controller.dto.TransactionDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "Transactions")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "currency_value")
    private Double amountInvested;
    ;

    @Column(name = "currency_quantity")
    private Double currencyQuantity;

    @OneToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "transactionStatus_id")
    private TransactionStatus transactionStatus;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "YYYY-MM-DD HH:MM:SS")
    private LocalDateTime createAt;

    @Column(name = "updated_at")
    @DateTimeFormat(pattern = "YYYY-MM-DD HH:MM:SS")
    private LocalDateTime updatedAt;

    public Transaction() {

    }

    public TransactionDto converter() {
        LocalDateTime now = this.getCreateAt().truncatedTo(ChronoUnit.SECONDS);
        return TransactionDto.builder()
                .id(this.getId())
                .createdAt(this.getCreateAt().toString())
                .transactionDate(now.toString())
                .walletId(this.getWallet().getId())
                .nameCurrency(this.getCurrency().getName())
                .amountInvested(this.getAmountInvested())
                .transactionName(this.getTransactionStatus().getStatus())
                .currencyQuantity(this.getCurrencyQuantity())
                .build();
    }
}
