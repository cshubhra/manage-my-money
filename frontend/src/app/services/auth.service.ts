import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { User } from '../models/user.model';
import { environment } from '../../environments/environment';

interface AuthResponse {
  token: string;
  user: User;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser$: Observable<User | null>;
  private tokenExpirationTimer: any;

  constructor(private http: HttpClient, private router: Router) {
    this.currentUserSubject = new BehaviorSubject<User | null>(null);
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  /**
   * Get the current user
   */
  public get currentUser(): User | null {
    return this.currentUserSubject.value;
  }

  /**
   * Check if the user is authenticated
   */
  public get isAuthenticated(): boolean {
    return !!this.currentUserSubject.value && !!localStorage.getItem('token');
  }

  /**
   * Login user with email and password
   */
  login(email: string, password: string): Observable<User> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, { email, password })
      .pipe(
        map(response => {
          // Store token and user in local storage and subject
          localStorage.setItem('token', response.token);
          const user = new User(response.user);
          this.currentUserSubject.next(user);
          this.autoLogout();
          return user;
        })
      );
  }

  /**
   * Register a new user
   */
  register(email: string, username: string, password: string): Observable<User> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, {
      email,
      username,
      password
    }).pipe(
      map(response => {
        // Store token and user in local storage and subject
        localStorage.setItem('token', response.token);
        const user = new User(response.user);
        this.currentUserSubject.next(user);
        this.autoLogout();
        return user;
      })
    );
  }

  /**
   * Logout the current user
   */
  logout(): void {
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
    if (this.tokenExpirationTimer) {
      clearTimeout(this.tokenExpirationTimer);
    }
    this.tokenExpirationTimer = null;
  }

  /**
   * Auto login from local storage token
   */
  autoLogin(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      return;
    }

    // Get user data from token
    this.http.get<User>(`${environment.apiUrl}/users/me`)
      .subscribe(
        (user: User) => {
          this.currentUserSubject.next(new User(user));
          this.autoLogout();
        },
        error => {
          this.logout();
        }
      );
  }

  /**
   * Set auto logout timer
   */
  autoLogout(): void {
    if (this.tokenExpirationTimer) {
      clearTimeout(this.tokenExpirationTimer);
    }
    
    // Set expiration to 24 hours for example
    const expirationDuration = 24 * 60 * 60 * 1000; 
    this.tokenExpirationTimer = setTimeout(() => {
      this.logout();
    }, expirationDuration);
  }

  /**
   * Get authentication token
   */
  getToken(): string | null {
    return localStorage.getItem('token');
  }
}