package br.com.safi.repository;

import br.com.safi.models.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionStatusRepository extends JpaRepository<TransactionStatus, Long> {
    TransactionStatus getById(Long id);
    TransactionStatus getByStatus(String status);
}
