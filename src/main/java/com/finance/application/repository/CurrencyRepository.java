package com.finance.application.repository;

import com.finance.application.model.Currency;
import com.finance.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Currency entity.
 */
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    
    /**
     * Find a currency by its long symbol (e.g., "USD").
     *
     * @param longSymbol the long symbol
     * @return the currency if found
     */
    Currency findByLongSymbol(String longSymbol);
    
    /**
     * Find a currency by its long symbol and user.
     *
     * @param longSymbol the long symbol
     * @param user the user
     * @return the currency if found
     */
    Optional<Currency> findByLongSymbolAndUser(String longSymbol, User user);
    
    /**
     * Find all currencies visible to a user (user's own currencies and system currencies).
     *
     * @param userId the user id
     * @return the list of visible currencies
     */
    @Query("SELECT c FROM Currency c WHERE c.user.id = ?1 OR c.user IS NULL")
    List<Currency> findVisibleCurrencies(Long userId);
    
    /**
     * Find all currencies used by a user in transfers within a date range.
     *
     * @param user the user
     * @param startDay the start date
     * @param endDay the end date
     * @return the list of currencies
     */
    @Query("SELECT DISTINCT c FROM Currency c " +
           "JOIN TransferItem ti ON ti.currency = c " +
           "JOIN ti.transfer t " +
           "WHERE (c.user = ?1 OR c.user IS NULL) " +
           "AND t.user = ?1 " +
           "AND t.day >= ?2 " +
           "AND t.day <= ?3")
    List<Currency> findByUserAndPeriod(User user, LocalDate startDay, LocalDate endDay);
}