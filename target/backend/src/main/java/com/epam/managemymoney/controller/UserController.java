package com.epam.managemymoney.controller;

import com.epam.managemymoney.dto.PasswordUpdateRequest;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import javax.validation.Valid;
import com.epam.managemymoney.service.UserService;
import com.epam.managemymoney.dto.UserDTO;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //@GetMapping("/current")

    @PutMapping("/current")
    public ResponseEntity<UserDTO> updateCurrentUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(userDTO.getId(),userDTO));
    }

    @PutMapping("/current/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody PasswordUpdateRequest request) {
        userService.changePassword(request.getUserID(),request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }
}