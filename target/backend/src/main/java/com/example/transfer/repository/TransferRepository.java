package com.example.transfer.repository;

import com.example.transfer.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByCreatedByOrderByCreatedAtDesc(String createdBy);
    List<Transfer> findByStatus(String status);
}