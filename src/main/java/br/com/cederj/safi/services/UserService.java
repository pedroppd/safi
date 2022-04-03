package br.com.cederj.safi.services;

import br.com.cederj.safi.models.User;
import br.com.cederj.safi.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) {
        try {
            log.debug("encoding password", user);
            this.encodePassword(user);
            log.debug("putting verification code", user);
            user.setVerificationCode(RandomString.make(64));
            return this.userRepository.save(user);
        } catch(Exception ex) {
            throw ex;
        }
    }


    private void encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

}
