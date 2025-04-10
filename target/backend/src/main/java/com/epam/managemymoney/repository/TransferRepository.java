package com.epam.managemymoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.epam.managemymoney.model.Transfer;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByUserId(Long userId);
    List<Transfer> findByUserIdAndCategoryId(Long userId, Long categoryId);
}