import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Goal, GoalProgress } from '../models/goal.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GoalService {
  private apiUrl = `${environment.apiUrl}/api/goals`;

  constructor(private http: HttpClient) {}

  getAllGoals(): Observable<Goal[]> {
    return this.http.get<Goal[]>(this.apiUrl);
  }

  getGoalById(id: number): Observable<Goal> {
    return this.http.get<Goal>(`${this.apiUrl}/${id}`);
  }

  createGoal(goal: Goal): Observable<Goal> {
    return this.http.post<Goal>(this.apiUrl, goal);
  }

  updateGoal(id: number, goal: Goal): Observable<Goal> {
    return this.http.put<Goal>(`${this.apiUrl}/${id}`, goal);
  }

  deleteGoal(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateProgress(id: number, progress: GoalProgress): Observable<Goal> {
    return this.http.post<Goal>(`${this.apiUrl}/${id}/progress`, progress);
  }

  getGoalProgress(id: number): Observable<GoalProgress[]> {
    return this.http.get<GoalProgress[]>(`${this.apiUrl}/${id}/progress`);
  }
}