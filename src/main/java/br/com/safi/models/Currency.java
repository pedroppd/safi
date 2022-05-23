package br.com.safi.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "currency")
@Getter
@Setter
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String name;

    @Column
    @OneToMany(mappedBy = "currency")
    private List<WalletCurrency> walletCurrency;

    public Currency(String name) {
        this.name = name;
    }

    public Currency() {

    }
}
