import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './user.model';
import { LoginData } from './login.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  registerUser(user: User): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/signup`, user);
  }

  loginUser(loginData: LoginData): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, loginData);
  }
}
