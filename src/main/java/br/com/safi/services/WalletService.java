package br.com.safi.services;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.configuration.security.exception.dto.ValidationException;
import br.com.safi.models.Wallet;
import br.com.safi.repository.IWalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Transactional
@Service
@Slf4j
public class WalletService {

    @Autowired
    private IWalletRepository walletRepository;

    public Wallet getById(Long id) throws DataBaseException {
        try {
            return walletRepository.getById(id);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }

    public Wallet save(Wallet wallet) throws DataBaseException {
        try {
            Wallet walletExist = walletRepository.getByName(wallet.getName());
            if(walletExist != null) {
                String errorMessage = "Wallet with name "+walletExist.getName()+" already exists !";
                log.error(errorMessage);
                throw new ValidationException(errorMessage);
            }
            return walletRepository.save(wallet);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }

    @Async
    public void delete(Long walletId) throws DataBaseException {
        try {
            CompletableFuture.runAsync(() -> walletRepository.deleteById(walletId));
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }

    public List<Wallet> getAll() throws DataBaseException {
        try {
           return walletRepository.findAll();
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }

    public List<Wallet> getAllWalletsByUserId(Long userId) throws DataBaseException {
        try {
            return walletRepository.findWalletByUser_Id(userId);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }
}
