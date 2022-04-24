package br.com.safi.services;

import br.com.safi.models.User;
import br.com.safi.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailSender;

    @Async
    public CompletableFuture<User> register(String tid, User user) throws MessagingException, IOException {
        try {
            log.debug("Encoding password...", "tid", tid, "user", user);
            this.encodePassword(user);
            log.debug("Inserting verification code", "tid", tid, "user", user);
            user.setVerificationCode(RandomString.make(64));
            User userSaved = this.userRepository.save(user);
            mailSender.sendVerificationEmail(userSaved);
            return CompletableFuture.completedFuture(userSaved);
        } catch(Exception ex) {
            throw ex;
        }
    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode).orElse(null);

        if (user == null || user.isEnabled()) {
            return false;
        } else{
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
    }

    private void encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public User getUserById(long userUd) {
        return this.userRepository.findById(userUd).orElse(null);
    }

}
