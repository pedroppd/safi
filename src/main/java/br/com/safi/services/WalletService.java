package br.com.safi.services;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.models.Wallet;
import br.com.safi.repository.IWalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
public class WalletService {

    @Autowired
    private IWalletRepository walletRepository;

    public Wallet getbyId(Long id) throws DataBaseException {
        try {
            return walletRepository.getById(id);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }

    public Wallet save(Wallet wallet) throws DataBaseException {
        try {
            return walletRepository.save(wallet);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }

    public void delete(Long walletId) throws DataBaseException {
        try {
            CompletableFuture.runAsync(() -> walletRepository.deleteById(walletId));
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }
}
