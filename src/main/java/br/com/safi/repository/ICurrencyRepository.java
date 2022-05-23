package br.com.safi.repository;

import br.com.safi.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICurrencyRepository extends JpaRepository<Currency, Long> {
    Currency save(Currency currency);

    Currency getById(Long id);

    Optional<Currency>  getByName(String name);

    @Query(nativeQuery = true, value = "select c.name from currency c inner join wallet_currency wc on wc.currency_id = c.id where wc.wallet_id = :walletId")
    List<String> getByWalletId(@Param(value = "walletId") Long walletId);
}
