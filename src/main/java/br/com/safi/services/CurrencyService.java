package br.com.safi.services;

import br.com.safi.models.Currency;
import br.com.safi.repository.ICurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrencyService {

    @Autowired
    ICurrencyRepository currencyRepository;

    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }
}
