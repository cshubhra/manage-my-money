import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { LoginRequest, LoginResponse, RegisterRequest, User, AuthState } from '../models/auth.model';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly API_URL = `${environment.apiUrl}/api/auth`;
    private authStateSubject: BehaviorSubject<AuthState>;
    public authState$: Observable<AuthState>;

    constructor(private http: HttpClient) {
        const initialState: AuthState = {
            isAuthenticated: false,
            user: null,
            token: null,
            error: null
        };
        this.authStateSubject = new BehaviorSubject<AuthState>(initialState);
        this.authState$ = this.authStateSubject.asObservable();
        
        // Check for existing token on startup
        const storedToken = localStorage.getItem('token');
        if (storedToken) {
            this.validateAndRestoreSession(storedToken);
        }
    }

    login(credentials: LoginRequest): Observable<LoginResponse> {
        return this.http.post<LoginResponse>(`${this.API_URL}/login`, credentials)
            .pipe(
                tap(response => this.handleAuthenticationSuccess(response)),
                catchError(error => this.handleAuthenticationError(error))
            );
    }

    register(userData: RegisterRequest): Observable<any> {
        return this.http.post(`${this.API_URL}/register`, userData)
            .pipe(
                catchError(error => this.handleAuthenticationError(error))
            );
    }

    logout(): void {
        localStorage.removeItem('token');
        this.authStateSubject.next({
            isAuthenticated: false,
            user: null,
            token: null,
            error: null
        });
    }

    private validateAndRestoreSession(token: string): void {
        this.http.get<User>(`${this.API_URL}/validate`)
            .pipe(
                catchError(() => {
                    this.logout();
                    return throwError(() => new Error('Invalid session'));
                })
            )
            .subscribe(user => {
                this.authStateSubject.next({
                    isAuthenticated: true,
                    user,
                    token,
                    error: null
                });
            });
    }

    private handleAuthenticationSuccess(response: LoginResponse): void {
        localStorage.setItem('token', response.token);
        this.authStateSubject.next({
            isAuthenticated: true,
            user: response.user,
            token: response.token,
            error: null
        });
    }

    private handleAuthenticationError(error: any): Observable<never> {
        this.authStateSubject.next({
            ...this.authStateSubject.value,
            error: error.message || 'Authentication failed'
        });
        return throwError(() => error);
    }

    isAuthenticated(): boolean {
        return this.authStateSubject.value.isAuthenticated;
    }

    getToken(): string | null {
        return this.authStateSubject.value.token;
    }

    getCurrentUser(): User | null {
        return this.authStateSubject.value.user;
    }
}