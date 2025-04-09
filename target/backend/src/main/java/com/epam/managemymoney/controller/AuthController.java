package com.epam.managemymoney.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.epam.managemymoney.service.AuthenticationService;
import com.epam.managemymoney.dto.AuthRequestDTO;
import com.epam.managemymoney.dto.AuthResponseDTO;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthenticationService authService;

    @Autowired
    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        log.info("Authentication request received for user: {}", request.getUsername());
        //return ResponseEntity.ok(authService.authenticate(request));
        return ResponseEntity.ok("true");
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody AuthRequestDTO request) {
        log.info("Registration request received for user: {}", request.getUsername());
        //return ResponseEntity.ok(authService.register(request));
        return ResponseEntity.ok("true");
    }
}