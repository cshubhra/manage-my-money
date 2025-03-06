package com.finance.application.controller;

import com.finance.application.dto.UserDTO;
import com.finance.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * GET /users : get all users.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of users in body
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    /**
     * GET /users/:id : get the "id" user.
     *
     * @param id the id of the user to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the user, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
    
    /**
     * POST /users : Create a new user.
     *
     * @param userDTO the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.save(userDTO));
    }
    
    /**
     * PUT /users/:id : Updates an existing user.
     *
     * @param id the id of the user to update
     * @param userDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        return ResponseEntity.ok(userService.update(userDTO));
    }
    
    /**
     * DELETE /users/:id : delete the "id" user.
     *
     * @param id the id of the user to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}