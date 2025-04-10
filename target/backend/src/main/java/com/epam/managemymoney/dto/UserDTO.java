package com.epam.managemymoney.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}