import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Goal } from '../models/goal.model';

@Injectable({
  providedIn: 'root'
})
export class GoalService {
  private apiUrl = '/api/goals';

  constructor(private http: HttpClient) {}

  getGoals(): Observable<Goal[]> {
    return this.http.get<Goal[]>(this.apiUrl);
  }

  getGoal(id: number): Observable<Goal> {
    return this.http.get<Goal>(`${this.apiUrl}/${id}`);
  }

  createGoal(goal: Goal): Observable<Goal> {
    return this.http.post<Goal>(this.apiUrl, goal);
  }

  updateGoal(id: number, goal: Goal): Observable<Goal> {
    return this.http.put<Goal>(`${this.apiUrl}/${id}`, goal);
  }

  updateProgress(id: number, progress: number): Observable<Goal> {
    return this.http.patch<Goal>(`${this.apiUrl}/${id}/progress`, { progress });
  }

  deleteGoal(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}