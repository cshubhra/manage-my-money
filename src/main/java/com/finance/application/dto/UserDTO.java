package com.finance.application.dto;

import com.finance.application.model.MultiCurrencyBalanceCalculatingAlgorithm;
import com.finance.application.model.TransactionAmountLimitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for User entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private Long id;
    
    @NotBlank
    @Size(min = 3, max = 40)
    private String login;
    
    @Size(max = 100)
    private String name;
    
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;
    
    private String password;
    
    private String activationCode;
    
    private LocalDateTime activatedAt;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private TransactionAmountLimitType transactionAmountLimitType;
    
    private Integer transactionAmountLimitValue;
    
    private boolean includeTransactionsFromSubcategories;
    
    private MultiCurrencyBalanceCalculatingAlgorithm multiCurrencyBalanceCalculatingAlgorithm;
    
    private boolean invertSaldoForIncome;
    
    private Long defaultCurrencyId;
}