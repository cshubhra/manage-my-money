package com.epam.managemymoney.service;

import com.epam.managemymoney.dto.UserDTO;
import com.epam.managemymoney.exception.DuplicateResourceException;
import com.epam.managemymoney.exception.ResourceNotFoundException;
import com.epam.managemymoney.model.User;
import com.epam.managemymoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating new user with email: {}", userDTO.getEmail());

        // Check if email already exists
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User", "email", userDTO.getEmail());
        }

        User user = modelMapper.map(userDTO, User.class);
        /* user.setPassword(passwordEncoder.encode(userDTO.getPassword())); */
        user.setPassword(userDTO.getPassword());
        //user.setActive(true);

        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getId());

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.debug("Fetching user by id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return modelMapper.map(user, UserDTO.class);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with id: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Check if email is being changed and if new email already exists
        if (!existingUser.getEmail().equals(userDTO.getEmail()) &&
                userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User", "email", userDTO.getEmail());
        }

        // Update fields
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setUsername(userDTO.getUsername());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            //existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            existingUser.setPassword(userDTO.getPassword());
        }
        //existingUser.setPreferredCurrency(userDTO.getPreferredCurrency());

        //existingUser.setTimeZone(userDTO.getTimeZone());
        //existingUser.setDateFormat(userDTO.getDateFormat());

        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully with id: {}", updatedUser.getId());

        return modelMapper.map(updatedUser, UserDTO.class);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userRepository.delete(user);
        log.info("User deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.debug("Fetching all users");

        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        log.info("Changing password for user with id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Verify old password
//        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
//            throw new IllegalArgumentException("Invalid current password");
//        }
        if (!oldPassword.equals(user.getPassword())) {
           throw new IllegalArgumentException("Invalid current password");
        }
       // user.setPassword(passwordEncoder.encode(newPassword));
        user.setPassword(newPassword);
        userRepository.save(user);
        log.info("Password changed successfully for user with id: {}", userId);
    }

    public void toggleUserStatus(Long userId) {
        log.info("Toggling status for user with id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        //user.setActive(!user.isActive());
        userRepository.save(user);
        //log.info("User status toggled to {} for user with id: {}", user.isActive(), userId);
    }

    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        log.debug("Checking email availability: {}", email);
        return userRepository.findByEmail(email).isEmpty();
    }

    public void updateUserPreferences(Long userId, UserDTO preferences) {
        log.info("Updating preferences for user with id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        //user.setPreferredCurrency(preferences.getPreferredCurrency());
        //user.setTimeZone(preferences.getTimeZone());
        //user.setDateFormat(preferences.getDateFormat());
        //user.setTheme(preferences.getTheme());
        //user.setLanguage(preferences.getLanguage());

        userRepository.save(user);
        log.info("Preferences updated successfully for user with id: {}", userId);
    }

    @Transactional(readOnly = true)
    public boolean validateCredentials(String email, String password) {
        log.debug("Validating credentials for email: {}", email);

        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.isPresent() &&
                //passwordEncoder.matches(password, userOpt.get().getPassword()) ;
                password.equals(userOpt.get().getPassword());
        //&&    userOpt.get().isActive();
    }
}