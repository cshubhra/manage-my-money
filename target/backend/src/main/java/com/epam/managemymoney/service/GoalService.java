package com.epam.managemymoney.service;

import com.epam.managemymoney.exception.ResourceNotFoundException;
import com.epam.managemymoney.model.Goal;
import com.epam.managemymoney.model.User;
import com.epam.managemymoney.repository.GoalRepository;
import com.epam.managemymoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public Goal createGoal(Goal goal, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        goal.setUser(user);
        goal.setCreatedDate(LocalDate.now());
        return goalRepository.save(goal);
    }

    public Goal updateGoal(Long goalId, Goal goalDetails) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));

        goal.setName(goalDetails.getName());
        goal.setDescription(goalDetails.getDescription());
        goal.setTargetAmount(goalDetails.getTargetAmount());
        goal.setTargetDate(goalDetails.getTargetDate());
        goal.setCurrentAmount(goalDetails.getCurrentAmount());
        goal.setPriority(goalDetails.getPriority());
        goal.setStatus(goalDetails.getStatus());

        return goalRepository.save(goal);
    }

    public void deleteGoal(Long goalId) {
        if (!goalRepository.existsById(goalId)) {
            throw new ResourceNotFoundException("Goal", "id", goalId);
        }
        goalRepository.deleteById(goalId);
    }

    @Transactional(readOnly = true)
    public Goal getGoal(Long goalId) {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));
    }

    @Transactional(readOnly = true)
    public List<Goal> getAllGoalsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return goalRepository.findByUserId(userId);
    }

    public Goal updateGoalProgress(Long goalId, BigDecimal amount) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));

        BigDecimal newAmount = goal.getCurrentAmount().add(amount);
        goal.setCurrentAmount(newAmount);

        // Update status based on progress
        if (newAmount.compareTo(goal.getTargetAmount()) >= 0) {
            goal.setStatus("COMPLETED");
        } else if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
            goal.setStatus("IN_PROGRESS");
        }

        return goalRepository.save(goal);
    }

    @Transactional(readOnly = true)
    public List<Goal> getActiveGoals(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return goalRepository.findByUserIdAndStatusNot(userId, "COMPLETED");
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateGoalProgress(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));

        if (goal.getTargetAmount().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return goal.getCurrentAmount()
                .divide(goal.getTargetAmount(), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    public List<Goal> getGoalsByPriority(Long userId, String priority) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return goalRepository.findByUserIdAndPriorityOrderByTargetDateAsc(userId, priority);
    }

    @Transactional(readOnly = true)
    public List<Goal> getUpcomingGoals(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        LocalDate currentDate = LocalDate.now();
        return goalRepository.findByUserIdAndTargetDateGreaterThanAndStatusNotOrderByTargetDateAsc(
                userId, currentDate, "COMPLETED");
    }
}