package br.com.safi.models;

import br.com.safi.controller.dto.TransactionDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "amount_invested", nullable = false)
    private Double amountInvested;

    @Column(name = "currency_quantity", nullable = false)
    private Double currencyQuantity;

    @OneToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "transactionStatus_id", nullable = false)
    private TransactionStatus transactionStatus;

    @Column(name = "transaction_date", nullable = false)
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
        String date = this.getTransactionDate().toString();
        return TransactionDto.builder()
                .id(this.getId())
                .createdAt(this.getCreateAt().toString())
                .transactionDate(date)
                .walletId(this.getWallet().getId())
                .nameCurrency(this.getCurrency().getName())
                .amountInvested(this.getAmountInvested())
                .transactionName(this.getTransactionStatus().getStatus())
                .currencyQuantity(this.getCurrencyQuantity())
                .build();
    }
}
