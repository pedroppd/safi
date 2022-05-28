package br.com.safi.services;

import br.com.safi.configuration.security.exception.dto.DataBaseException;
import br.com.safi.configuration.security.exception.dto.PersistDataException;
import br.com.safi.configuration.security.exception.dto.ValidationException;
import br.com.safi.models.User;
import br.com.safi.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailSender;

    @Async
    public CompletableFuture<User> register(String tid, User user) throws DataBaseException {
        try {
            log.debug("Verifying if user exist...");
            User userExist = this.getUserByEmail(user.getEmail());
            if (userExist != null) {
                String errorMessage = "Já existe um usuário cadastrado com esse email " + userExist.getEmail() + "!!";
                log.error(errorMessage);
                throw new ValidationException(errorMessage);
            }
            log.debug("Encoding password...", "tid", tid, "user", user);
            this.encodePassword(user);
            log.debug("Inserting verification code", "tid", tid, "user", user);
            user.setVerificationCode(RandomString.make(64));
            User userSaved = this.userRepository.save(user);
            mailSender.sendVerificationEmail(userSaved);
            return CompletableFuture.completedFuture(userSaved);
        } catch (Exception ex) {
            log.error(ex.getMessage(), "stack", ex.getStackTrace());
            throw new DataBaseException(ex.getMessage());
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode).orElse(null);
        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
    }

    public void encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public User getUserById(long userUd) {
        return this.userRepository.findById(userUd).orElse(null);
    }

    public User getUserByVerificationCode(String verificationCode) {
        return this.userRepository.findByVerificationCode(verificationCode).orElse(null);
    }

    public void deleteUserById(long id) {
        this.userRepository.deleteById(id);
    }

    public void updateUser(User user) throws PersistDataException {
        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new PersistDataException(500, ex.getMessage());
        }
    }
}
