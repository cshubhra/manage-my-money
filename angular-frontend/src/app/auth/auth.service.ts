import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

import { User, AuthResponse } from '../models/user.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser$: Observable<User | null>;

  constructor(private http: HttpClient, private router: Router) {
    const storedUser = localStorage.getItem('currentUser');
    this.currentUserSubject = new BehaviorSubject<User | null>(
      storedUser ? JSON.parse(storedUser) : null
    );
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  /**
   * Login the user and store token
   */
  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, { username, password })
      .pipe(
        tap(response => {
          // Store token and user info
          localStorage.setItem('token', response.token);
          localStorage.setItem('currentUser', JSON.stringify(response.user));
          this.currentUserSubject.next(response.user);
        })
      );
  }

  /**
   * Register a new user
   */
  register(user: User, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/register`, {
      username: user.username,
      password: password,
      name: user.name,
      email: user.email
    });
  }

  /**
   * Logout the current user
   */
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  /**
   * Check if user is authenticated
   */
  isAuthenticated(): boolean {
    return !!localStorage.getItem('token') && !!this.currentUserSubject.value;
  }

  /**
   * Get current user
   */
  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  /**
   * Check authentication status from token in localStorage
   */
  checkAuthentication(): void {
    const token = localStorage.getItem('token');
    if (token) {
      // Token exists, validate it by fetching current user
      this.validateToken().subscribe();
    }
  }

  /**
   * Validate token by getting current user
   */
  private validateToken(): Observable<User | null> {
    return this.http.get<User>(`${environment.apiUrl}/users/current`)
      .pipe(
        tap(user => {
          localStorage.setItem('currentUser', JSON.stringify(user));
          this.currentUserSubject.next(user);
        }),
        catchError(error => {
          this.logout();
          return of(null);
        })
      );
  }

  /**
   * Activate user account
   */
  activate(activationCode: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/activate/${activationCode}`);
  }
}