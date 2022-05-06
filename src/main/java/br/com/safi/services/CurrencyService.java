package br.com.safi.services;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.configuration.security.exception.dto.PersistDataException;
import br.com.safi.models.Currency;
import br.com.safi.repository.ICurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CurrencyService {

    @Autowired
    private ICurrencyRepository currencyRepository;

    public Currency save(Currency currency) throws PersistDataException {
        try {
            currency.setName(currency.getName().toUpperCase());
            return currencyRepository.save(currency);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new PersistDataException(ex.getMessage());
        }
    }

    public Currency getById(Long id) throws DataBaseException {
        try {
            return currencyRepository.getById(id);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }

    public Currency getByName(String name) throws DataBaseException {
        try {
            return currencyRepository.getByName(name).orElse(null);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }
}
