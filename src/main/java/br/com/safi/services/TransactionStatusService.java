package br.com.safi.services;

import br.com.safi.configuration.security.exception.dto.GetDataException;
import br.com.safi.models.TransactionStatus;
import br.com.safi.repository.ITransactionStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class TransactionStatusService {

    private final ITransactionStatusRepository transactionStatusRepository;

    public TransactionStatus getById(Long id) throws GetDataException {
        try {
            return transactionStatusRepository.getById(id);
        }catch (Exception ex) {
            log.error(ex.getMessage());
            throw new GetDataException(ex.getMessage());
        }
    }

    public TransactionStatus getByName(String transactionName) throws GetDataException {
        try {
            return transactionStatusRepository.getByStatus(transactionName);
        }catch (Exception ex) {
            log.error(ex.getMessage());
            throw new GetDataException(ex.getMessage());
        }
    }
}
