package br.com.safi.controller.dto;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.configuration.security.exception.dto.MethodNotImplementException;import br.com.safi.models.Currency;
import br.com.safi.services.WalletService;
import java.util.Map;

public abstract class AbstractConverter<T> {
    public T converter() throws MethodNotImplementException {
        throw new MethodNotImplementException("Method not implement exception");
    }

    public T converter(Map<String, Currency> currencies, WalletService walletService) throws DataBaseException, MethodNotImplementException {
        throw new MethodNotImplementException("Method not implement exception");
    }
}
