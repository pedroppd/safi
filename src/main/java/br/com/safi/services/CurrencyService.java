package br.com.safi.services;

import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.configuration.security.exception.dto.PersistDataException;
import br.com.safi.models.Currency;
import br.com.safi.repository.ICurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Currency getById(Long id) throws GetDataException {
        try {
            return currencyRepository.getById(id);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new GetDataException(ex.getMessage());
        }
    }

    public Currency getByName(String name) throws GetDataException {
        try {
            return currencyRepository.getByName(name).orElse(null);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new GetDataException(ex.getMessage());
        }
    }

    public List<Currency> getAll() throws GetDataException {
        try {
            return currencyRepository.findAll();
        } catch (Exception ex) {
            log.error(ex.getMessage(), "error", ex, "stack", ex.getStackTrace());
            throw new GetDataException(ex.getMessage());
        }
    }
}
