package com.moneytransfer.repository;

import com.moneytransfer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity
 * Provides CRUD operations and custom queries for User entities
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by login
     * 
     * @param login user login
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByLogin(String login);
    
    /**
     * Find a user by email
     * 
     * @param email user email
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if a user exists with the given login
     * 
     * @param login user login
     * @return true if user exists
     */
    boolean existsByLogin(String login);
    
    /**
     * Check if a user exists with the given email
     * 
     * @param email user email
     * @return true if user exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Find a user by activation code
     * 
     * @param activationCode activation code
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByActivationCode(String activationCode);
    
    /**
     * Find a user by remember token
     * 
     * @param rememberToken remember token
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByRememberToken(String rememberToken);
}