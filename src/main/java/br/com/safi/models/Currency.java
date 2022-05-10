package br.com.safi.models;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

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

    public Currency(String name) {
        this.name = name;
    }

    public Currency() {

    }
}
