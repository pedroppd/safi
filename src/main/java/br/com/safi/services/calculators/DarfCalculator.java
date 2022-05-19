package br.com.safi.services.calculators;

import br.com.safi.models.Transaction;
import br.com.safi.services.interfaces.ICalcTax;

import java.util.List;

public class DarfCalculator implements ICalcTax {

    @Override
    public void calcTax(List<Transaction> transactionList) {
        //TODO: Fazer cálculo do DARF.
        transactionList.stream();
    }
}
