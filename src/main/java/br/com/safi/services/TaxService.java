package br.com.safi.services;

import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.controller.dto.DarfDto;
import br.com.safi.models.Transaction;
import br.com.safi.services.interfaces.ICalcTax;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaxService {

    public List<DarfDto> calcTax(List<Transaction> transactionList, int year, ICalcTax calcTax) throws GetDataException {
        return calcTax.calcTax(transactionList, year);
    }
}
