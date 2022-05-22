package br.com.safi.services.interfaces;

import br.com.safi.controller.dto.DarfDto;
import br.com.safi.models.Transaction;

import java.util.List;

public interface ICalcTax {
    List<DarfDto> calcTax(List<Transaction> transactionList);
}
