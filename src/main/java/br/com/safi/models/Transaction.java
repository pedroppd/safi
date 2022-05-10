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

    private BigDecimal inputValue;

    private BigDecimal outputValue;

    @OneToOne
    @JoinColumn(name = "inputCurrency_id")
    private Currency inputCurrency;

    @OneToOne
    @JoinColumn(name = "outputCurrency_id")
    private Currency outputCurrency;

    @ManyToOne
    @JoinColumn(name = "transactionStatus_id")
    private TransactionStatus transactionStatus;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "YYYY-MM-DD HH:MM:SS")
    private LocalDateTime createAt;

    public Transaction() {

    }

    public TransactionDto converter() {
        return TransactionDto.builder()
                .transactionDate(this.getCreateAt())
                .outputValue(this.getOutputValue())
                .inputValue(this.getInputValue())
                .walletId(this.getWallet().getId())
                .inputNameCurrency(this.getInputCurrency().getName())
                .outputNameCurrency(this.getOutputCurrency().getName())
                .build();
    }

}
