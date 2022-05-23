package br.com.safi.repository;

import br.com.safi.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITransactionRespository extends JpaRepository<Transaction, Long> {

    Transaction save(Transaction transaction);

    List<Transaction> getTransactionByWalletIdOrderByTransactionDateDesc(Long id);

    List<Transaction> getTransactionByTransactionStatus_IdAndCurrency_Id(Long TransactionStatusId, Long CurrencyId);

    @Query(nativeQuery = true, value = "SELECT * FROM transactions t INNER JOIN wallets w ON t.wallet_id = w.id WHERE w.id = :walletId order by t.transaction_date,t.id ASC")
    List<Transaction> getTransactionByWalletId(@Param(value = "walletId") Long walletId);

    Optional<Transaction> getTransactionById(Long transactionId);
}
