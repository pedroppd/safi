package br.com.safi.repository;

import br.com.safi.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRespository extends JpaRepository<Transaction, Long> {

    Transaction save(Transaction transaction);

    List<Transaction> getTransactionByWalletId(Long id);

    List<Transaction> getTransactionByTransactionStatus_IdAndAndCurrency_Id(Long TransactionStatusId, Long CurrencyId);
}
