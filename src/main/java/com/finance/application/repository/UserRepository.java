package com.finance.application.repository;

import com.finance.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by login.
     *
     * @param login the login
     * @return the user if found
     */
    Optional<User> findByLogin(String login);
    
    /**
     * Find a user by email.
     *
     * @param email the email
     * @return the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find a user by activation code.
     *
     * @param activationCode the activation code
     * @return the user if found
     */
    Optional<User> findByActivationCode(String activationCode);
    
    /**
     * Check if a user exists by login.
     *
     * @param login the login
     * @return true if exists, false otherwise
     */
    boolean existsByLogin(String login);
    
    /**
     * Check if a user exists by email.
     *
     * @param email the email
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
}