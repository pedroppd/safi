package br.com.cederj.safi.repository;

import br.com.cederj.safi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
