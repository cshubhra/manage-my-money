package com.finance.application.mapper;

import com.finance.application.dto.UserDTO;
import com.finance.application.model.Currency;
import com.finance.application.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for converting between User entity and UserDTO.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    
    /**
     * Convert User entity to UserDTO.
     *
     * @param user the User entity
     * @return the UserDTO
     */
    @Mapping(source = "user.defaultCurrency", target = "defaultCurrencyId", qualifiedByName = "currencyToId")
    UserDTO toDto(User user);
    
    /**
     * Convert UserDTO to User entity.
     *
     * @param userDTO the UserDTO
     * @return the User entity
     */
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "transfers", ignore = true)
    @Mapping(target = "currencies", ignore = true)
    @Mapping(target = "exchanges", ignore = true)
    @Mapping(target = "goals", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "defaultCurrency", ignore = true)
    User toEntity(UserDTO userDTO);
    
    /**
     * Extract currency ID from Currency entity.
     *
     * @param currency the Currency entity
     * @return the Currency ID
     */
    @Named("currencyToId")
    default Long currencyToId(Currency currency) {
        return currency != null ? currency.getId() : null;
    }
}