package br.com.safi.controller;

import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.controller.dto.DarfDto;
import br.com.safi.models.Transaction;
import br.com.safi.services.calculators.DarfCalculator;
import br.com.safi.services.TaxService;
import br.com.safi.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/darf")
public class TaxController {

    @Autowired
    private TaxService taxService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{walletId}/wallet/{year}/year")
    public ResponseEntity<List<DarfDto>> calcTax(@PathVariable(value = "walletId") Long walletId, @PathVariable(value = "year") int year) throws GetDataException {
        List<Transaction> transactionList = transactionService.getTransactionByWalletIdAndYear(walletId, year);
        List<DarfDto> taxResult = taxService.calcTax(transactionList, new DarfCalculator());
        return ResponseEntity.ok().body(taxResult);
    }
}
