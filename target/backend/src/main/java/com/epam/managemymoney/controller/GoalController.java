package com.epam.managemymoney.controller;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import javax.validation.Valid;
import com.epam.managemymoney.service.GoalService;
import com.epam.managemymoney.dto.GoalDTO;
import com.epam.managemymoney.dto.ProgressUpdateRequest;

@RestController
@RequestMapping("/api/goals")
@Slf4j
public class GoalController {
    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public ResponseEntity<List<GoalDTO>> getAllGoals() {
        return ResponseEntity.ok(goalService.getAllGoals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalDTO> getGoal(@PathVariable Long id) {
        return ResponseEntity.ok(goalService.getGoalById(id));
    }

    @PostMapping
    public ResponseEntity<GoalDTO> createGoal(@Valid @RequestBody GoalDTO goal) {
        GoalDTO createdGoal = goalService.createGoal(goal);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGoal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoalDTO> updateGoal(@PathVariable Long id, @Valid @RequestBody GoalDTO goal) {
        return ResponseEntity.ok(goalService.updateGoal(id, goal));
    }

    @PatchMapping("/{id}/progress")
    public ResponseEntity<GoalDTO> updateProgress(
            @PathVariable Long id,
            @Valid @RequestBody ProgressUpdateRequest request) {
        return ResponseEntity.ok(goalService.updateProgress(id, request.getAmount()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }
}