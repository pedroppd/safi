package br.com.safi.models;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.configuration.security.exception.dto.MethodNotImplementException;
import br.com.safi.services.TransactionStatusService;
import br.com.safi.services.WalletService;

import java.util.Map;

public abstract class AbstractConverter<T> {
    public T converter() throws MethodNotImplementException {
        throw new MethodNotImplementException("Method not implement exception");
    }

    public T converter(Map<String, Currency> currencies, WalletService walletService, TransactionStatusService transactionStatusService) throws DataBaseException, MethodNotImplementException, GetDataException {
        throw new MethodNotImplementException("Method not implement exception");
    }
}
