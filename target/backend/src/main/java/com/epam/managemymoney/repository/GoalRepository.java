package com.epam.managemymoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.epam.managemymoney.model.Goal;
import com.epam.managemymoney.model.GoalStatus;
import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUserId(Long userId);
    List<Goal> findByUserIdAndStatus(Long userId, GoalStatus status);
}