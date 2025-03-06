package com.finance.application.service;

import com.finance.application.dto.UserDTO;
import com.finance.application.exception.ResourceNotFoundException;
import com.finance.application.mapper.UserMapper;
import com.finance.application.model.Category;
import com.finance.application.model.CategoryType;
import com.finance.application.model.Currency;
import com.finance.application.model.User;
import com.finance.application.repository.CurrencyRepository;
import com.finance.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing users.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Get all users.
     *
     * @return the list of users
     */
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get one user by id.
     *
     * @param id the id of the user
     * @return the user
     * @throws ResourceNotFoundException if the user is not found
     */
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    
    /**
     * Save a new user.
     *
     * @param userDTO the user to save
     * @return the saved user
     */
    public UserDTO save(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Generate activation code
        user.setActivationCode(UUID.randomUUID().toString());
        
        // Set default currency
        Currency defaultCurrency = currencyRepository.findById(userDTO.getDefaultCurrencyId())
                .orElseGet(() -> currencyRepository.findByLongSymbol("EUR"));
        user.setDefaultCurrency(defaultCurrency);
        
        // Create top-level categories
        createTopCategories(user);
        
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
    
    /**
     * Update an existing user.
     *
     * @param userDTO the user to update
     * @return the updated user
     * @throws ResourceNotFoundException if the user is not found
     */
    public UserDTO update(UserDTO userDTO) {
        User existingUser = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userDTO.getId()));
        
        User user = userMapper.toEntity(userDTO);
        
        // Don't update password if it's empty
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // Set default currency
        if (userDTO.getDefaultCurrencyId() != null) {
            Currency defaultCurrency = currencyRepository.findById(userDTO.getDefaultCurrencyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + userDTO.getDefaultCurrencyId()));
            user.setDefaultCurrency(defaultCurrency);
        } else {
            user.setDefaultCurrency(existingUser.getDefaultCurrency());
        }
        
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
    
    /**
     * Delete a user.
     *
     * @param id the id of the user to delete
     * @throws ResourceNotFoundException if the user is not found
     */
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }
    
    /**
     * Create top-level categories for a new user.
     *
     * @param user the user to create categories for
     */
    private void createTopCategories(User user) {
        // Create top-level categories
        createTopCategory(user, "Assets", CategoryType.ASSET);
        createTopCategory(user, "Income", CategoryType.INCOME);
        createTopCategory(user, "Expenses", CategoryType.EXPENSE);
        createTopCategory(user, "Loans", CategoryType.LOAN);
        createTopCategory(user, "Opening Balances", CategoryType.BALANCE);
    }
    
    /**
     * Create a top-level category.
     *
     * @param user the user to create the category for
     * @param name the name of the category
     * @param categoryType the type of the category
     */
    private void createTopCategory(User user, String name, CategoryType categoryType) {
        Category category = Category.builder()
                .name(name)
                .description(name)
                .categoryType(categoryType)
                .user(user)
                .build();
        user.getCategories().add(category);
    }
}