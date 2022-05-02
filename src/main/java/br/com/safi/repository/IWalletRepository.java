package br.com.safi.repository;

import br.com.safi.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWalletRepository extends JpaRepository<Wallet, Long> {

    Wallet getById(Long id);
}
