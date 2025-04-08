import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

interface LoginResponse {
  token: string;
  user: any;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<any>(null);
  private isLoggedInSubject = new BehaviorSubject<boolean>(false);

  currentUser$ = this.currentUserSubject.asObservable();
  isLoggedIn$ = this.isLoggedInSubject.asObservable();

  constructor(private http: HttpClient) {
    this.checkAuthStatus();
  }

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, { email, password })
      .pipe(
        tap(response => {
          localStorage.setItem('token', response.token);
          this.currentUserSubject.next(response.user);
          this.isLoggedInSubject.next(true);
        })
      );
  }

  logout(): void {
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
    this.isLoggedInSubject.next(false);
  }

  private checkAuthStatus(): void {
    const token = localStorage.getItem('token');
    if (token) {
      // Verify token validity with backend
      this.http.get(`${environment.apiUrl}/auth/verify`)
        .subscribe({
          next: (user: any) => {
            this.currentUserSubject.next(user);
            this.isLoggedInSubject.next(true);
          },
          error: () => this.logout()
        });
    }
  }
}