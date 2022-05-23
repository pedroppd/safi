package br.com.safi.services.calculators;

import lombok.Data;

@Data
public class CurrencyHistory {
    private Double quantity;
    private Double investedValue;
    private String name;

    public CurrencyHistory(String name, Double currentQuantity, Double currentValueTotal) {
        this.quantity = currentQuantity;
        this.name = name;
        this.investedValue = currentValueTotal;
    }
}
