package br.com.safi.models;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "currency")
@Getter
@Setter
@Builder
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @OneToMany(mappedBy = "currency")
    private Set<WalletCurrency> registrations;
}
