package br.com.safi.repository;

import br.com.safi.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionRespository extends JpaRepository<Transaction, Long> {

    Transaction save(Transaction transaction);
}
