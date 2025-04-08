package com.modernization.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    
    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
    private String name;
    
    @NotBlank(message = "Category description is required")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}