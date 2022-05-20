package br.com.safi.repository;

import br.com.safi.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRespository extends JpaRepository<Transaction, Long> {

    Transaction save(Transaction transaction);

    List<Transaction> getTransactionByWalletId(Long id);

    List<Transaction> getTransactionByTransactionStatus_IdAndCurrency_Id(Long TransactionStatusId, Long CurrencyId);

    @Query(nativeQuery = true, value = "SELECT * FROM transactions t INNER JOIN wallets w ON t.wallet_id = w.id WHERE w.id = :walletId and YEAR(t.transaction_date) = :year order by transaction_date DESC")
    List<Transaction> getTransactionByWalletIdAndYear(@Param(value = "walletId") Long walletId, @Param(value = "year") int year);

//    @Query(nativeQuery = true, value = "SELECT * FROM transactions t INNER JOIN wallets w ON t.wallet_id = w.id WHERE w.id = :walletId and YEAR(t.transaction_date) = :year order by transaction_date DESC")
//    List<Transaction> getTransactionByWalletIdAndYear(@Param(value = "walletId") Long walletId, @Param(value = "year") int year);
//
//    @Query(nativeQuery = true, value = "SELECT * FROM transactions t INNER JOIN wallets w ON t.wallet_id = w.id WHERE w.id = :walletId and YEAR(t.transaction_date) = :year order by transaction_date DESC")
//    List<Transaction> getTransactionByWalletIdAndYear(@Param(value = "walletId") Long walletId, @Param(value = "year") int year);
}
