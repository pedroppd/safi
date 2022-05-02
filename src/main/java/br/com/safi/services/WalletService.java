package br.com.safi.services;

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

    public Wallet getbyId(Long id) {
        try {
            return walletRepository.getById(id);
        }catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw ex;
        }
    }

    public Wallet save(Wallet wallet) {
        try {
            return walletRepository.save(wallet);
        }catch (Exception ex) {
            throw ex;
        }
    }

    public void delete(Long walletId) {
        try {
             CompletableFuture.runAsync(() -> walletRepository.deleteById(walletId));
        }catch (Exception ex) {
            throw ex;
        }
    }
}
