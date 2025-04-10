package com.epam.managemymoney.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateRequest {

    @NotBlank(message = "Current userid is required")
    private Long userID;

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String newPassword;

    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;

    public boolean isPasswordsMatch() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}