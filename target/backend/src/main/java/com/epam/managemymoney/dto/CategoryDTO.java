package com.epam.managemymoney.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
}