import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-goal-progress',
  template: `
    <div class="goal-progress-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Goal Progress Tracking</mat-card-title>
        </mat-card-header>
        
        <mat-card-content>
          <div *ngIf="loading" class="loading-spinner">
            <mat-spinner></mat-spinner>
          </div>
          
          <div *ngIf="!loading" class="goals-list">
            <div *ngFor="let goal of goals" class="goal-item">
              <mat-card>
                <mat-card-header>
                  <mat-card-title>{{ goal.title }}</mat-card-title>
                  <mat-card-subtitle>
                    Target: {{ goal.targetAmount | currency }}
                  </mat-card-subtitle>
                </mat-card-header>
                
                <mat-card-content>
                  <mat-progress-bar
                    [mode]="'determinate'"
                    [value]="goal.progress"
                  ></mat-progress-bar>
                  
                  <div class="progress-details">
                    <span>Current: {{ goal.currentAmount | currency }}</span>
                    <span>Progress: {{ goal.progress }}%</span>
                  </div>
                  
                  <div class="goal-dates">
                    <span>Start: {{ goal.startDate | date }}</span>
                    <span>End: {{ goal.endDate | date }}</span>
                  </div>
                </mat-card-content>
                
                <mat-card-actions>
                  <button mat-button (click)="updateProgress(goal)">
                    Update Progress
                  </button>
                  <button mat-button (click)="viewDetails(goal)">
                    View Details
                  </button>
                </mat-card-actions>
              </mat-card>
            </div>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .goal-progress-container {
      padding: 20px;
    }
    
    .goals-list {
      display: grid;
      gap: 20px;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    }
    
    .goal-item {
      margin-bottom: 20px;
    }
    
    .progress-details {
      display: flex;
      justify-content: space-between;
      margin: 10px 0;
    }
    
    .goal-dates {
      display: flex;
      justify-content: space-between;
      margin-top: 10px;
      font-size: 0.9em;
      color: rgba(0, 0, 0, 0.6);
    }
    
    .loading-spinner {
      display: flex;
      justify-content: center;
      padding: 20px;
    }
  `]
})
export class GoalProgressComponent implements OnInit {
  goals: any[] = [];
  loading = false;

  constructor(
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.loadGoals();
  }

  loadGoals() {
    this.loading = true;
    // TODO: Implement service call to load goals
    this.loading = false;
  }

  updateProgress(goal: any) {
    // TODO: Implement progress update logic
  }

  viewDetails(goal: any) {
    // TODO: Implement navigation to detail view
  }
}