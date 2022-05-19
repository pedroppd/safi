package br.com.safi.services.interfaces;

import br.com.safi.models.Transaction;

import java.util.List;

public interface ICalcTax {
    void calcTax(List<Transaction> transactionList);
}
