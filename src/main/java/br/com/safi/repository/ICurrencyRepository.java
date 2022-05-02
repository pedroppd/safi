package br.com.safi.repository;

import br.com.safi.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICurrencyRepository extends JpaRepository<Currency, Long> {
    Currency save(Currency currency);

    Currency getById(Long id);
}
