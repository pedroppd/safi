package br.com.safi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "wallet_currency")
@Data
@Builder
@AllArgsConstructor
public class WalletCurrency implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private Double averagePrice;

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;

    public WalletCurrency() {

    }

    @Override
    public String toString() {
        return "WalletCurrency{" +
                "id=" + id +
                ", wallet=" + "" +
                ", currency=" + currency +
                ", quantity=" + quantity +
                ", averagePrice=" + averagePrice +
                ", createAt=" + createAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
