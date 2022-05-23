package br.com.safi.services.interfaces;

import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.controller.dto.DarfDto;
import br.com.safi.models.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ICalcTax {
    List<DarfDto> calcTax(List<Transaction> transactionList, int year) throws GetDataException;
}
