package br.com.safi.repository;

import br.com.safi.models.WalletCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWalletCurrencyRepository extends JpaRepository<WalletCurrency, Long> {

    WalletCurrency getWalletCurrencyByWallet_IdAndCurrency_Id(Long walletId, Long currencyId);

    List<WalletCurrency> getWalletCurrencyByWalletId(Long walletId);
}
